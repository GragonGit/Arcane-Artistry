package gragongit.arcaneartistry.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import gragongit.arcaneartistry.common.registry.RegistryUtils;
import gragongit.arcaneartistry.common.staff.StaffInteractionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

@Mixin(Item.class)
public abstract class StaffItemMixin {

  @Inject(method = "use", at = @At("HEAD"), cancellable = true)
  private void arcaneartistry$staffStart(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
    ItemStack stack = player.getItemInHand(hand);
    if (!RegistryUtils.isStaff(level, stack.getItem())) {
      return;
    }

    StaffInteractionEvents.START.invoker().onStaffInteractionStart(player);
    cir.setReturnValue(InteractionResult.CONSUME);
  }

  @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
  private void arcaneartistry$staffDuration(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
    if (RegistryUtils.isStaff(entity.level(), stack.getItem())) {
      cir.setReturnValue(72000);
    }
  }

  @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
  private void arcaneartistry$staffAnimation(ItemStack stack, CallbackInfoReturnable<ItemUseAnimation> cir) {
    if (RegistryUtils.isStaff(Minecraft.getInstance().level, stack.getItem())) {
      cir.setReturnValue(ItemUseAnimation.SPYGLASS);
    }
  }

  @Inject(method = "onUseTick", at = @At("HEAD"))
  private void arcaneartistry$staffHold(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration, CallbackInfo ci) {
    if (!RegistryUtils.isStaff(level, stack.getItem())) {
      return;
    }

    if (entity instanceof Player player) {
      StaffInteractionEvents.HOLD.invoker().onStaffInteractionHold(player);
    }
  }

  @Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
  private void arcaneartistry$staffStop(ItemStack stack, Level level, LivingEntity entity, int timeCharged,
      CallbackInfoReturnable<Boolean> cir) {
    if (!RegistryUtils.isStaff(level, stack.getItem())) {
      return;
    }

    if (entity instanceof Player player) {
      StaffInteractionEvents.STOP.invoker().onStaffInteractionStop(player);
    }
    cir.setReturnValue(false);
  }
}
