package gragongit.arcaneartistry.common.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.logging.LogUtils;
import gragongit.arcaneartistry.common.registry.ModRegistries;
import gragongit.arcaneartistry.common.staff.StaffDefinition;
import gragongit.arcaneartistry.common.staff.StaffType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(Item.class)
public abstract class ItemMixin {
  private static final Logger LOGGER = LogUtils.getLogger();

  @Inject(method = "use", at = @At("HEAD"))
  private void arcaneArtistry$onUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
    ItemStack stack = player.getItemInHand(hand);
    Identifier itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());

    Registry<StaffDefinition> staffs = player.level().registryAccess().lookupOrThrow(ModRegistries.STAFF_KEY);
    StaffDefinition definition = staffs.getValue(itemId);

    if (definition == null) {
      LOGGER.info("No staff definition");
      return;
    }

    StaffType type = definition.type();
    Identifier typeId = ModRegistries.STAFF_TYPES.getKey(type);

    LOGGER.info("Staff swung: item={}, staffType={}", itemId, typeId);
  }
}
