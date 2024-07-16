package de.arcane_artistry.item.staff;

import java.util.List;

import de.arcane_artistry.callback.cast.CastEndCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DebugStaffItem extends StaffItem {
  public StaffType staffType;

  public DebugStaffItem(Settings settings, StaffType staffType) {
    super(settings, staffType, false);
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    user.setCurrentHand(hand);

    if (!world.isClient()) {
      CastEndCallback.EVENT.invoker().interact(world, user, user.getActiveHand(), this, List.of());
    }

    return TypedActionResult.consume(user.getStackInHand(hand));
  }

  @Override
  public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

  }

  @Override
  public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {

  }

  @Override
  public int getMaxUseTime(ItemStack stack) {
    return 0;
  }

  @Override
  public boolean isUsedOnRelease(ItemStack stack) {
    return false;
  }
}
