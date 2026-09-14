package gragongit.arcaneartistry.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import gragongit.arcaneartistry.common.staff.StaffInteractionHandler;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemInHandRenderer.class)
public class CastingHandRendererMixin {
  @Unique
  private static final double MAX_MOVEMENT_RANGE = 67.0;
  @Unique
  private static final double MOVEMENT_TRANSLATE_SCALE = 0.0005;
  @Unique
  private static final float STAFF_CENTER_POS_X = 0.36F;
  @Unique
  private static final float STAFF_CENTER_POS_Y = -0.52F;
  @Unique
  private static final float STAFF_CENTER_POS_Z = -0.72F;
  @Unique
  private static final float STAFF_ROT_X = -70.0F;
  @Unique
  private static final float STAFF_ROT_Y = 10.0F;

  @Inject(method = "renderItem", at = @At("HEAD"))
  private void arcaneartistry$applyStaffCursorOffset(LivingEntity mob, ItemStack itemStack, ItemDisplayContext type, PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
    StaffInteractionHandler handler = StaffInteractionHandler.getInstance();

    if (handler == null || !handler.isCasting() || !type.firstPerson()) {
      return;
    }

    if (!(mob instanceof Player player) || itemStack != player.getItemInHand(handler.getCastingHand())) {
      return;
    }

    if (!player.isUsingItem() || player.getUseItemRemainingTicks() <= 0) {
      return;
    }

    HumanoidArm arm = player.getUsedItemHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
    int invert = arm == HumanoidArm.RIGHT ? 1 : -1;

    double offsetX = Mth.clamp(handler.getDeltaX() * MOVEMENT_TRANSLATE_SCALE, -MAX_MOVEMENT_RANGE, MAX_MOVEMENT_RANGE);
    double offsetY = Mth.clamp(handler.getDeltaY() * MOVEMENT_TRANSLATE_SCALE, -MAX_MOVEMENT_RANGE, MAX_MOVEMENT_RANGE);
    poseStack.translate(STAFF_CENTER_POS_X + offsetX, STAFF_CENTER_POS_Y + -offsetY, STAFF_CENTER_POS_Z);
    poseStack.mulPose(Axis.XP.rotationDegrees(STAFF_ROT_X));
    poseStack.mulPose(Axis.YP.rotationDegrees(invert * STAFF_ROT_Y));
  }
}
