package de.arcane_artistry.item.staff;

import de.arcane_artistry.cast.CastHandler;
import de.arcane_artistry.cast.client.CastInputHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class StaffItem extends Item {
  public StaffType staffType;
  private final boolean canHoldSpells;

  public StaffItem(Settings settings, StaffType staffType, boolean canHoldSpells) {
    super(settings);
    this.staffType = staffType;
    this.canHoldSpells = canHoldSpells;
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    user.setCurrentHand(hand);

    if (world.isClient()) {
      CastInputHandler.startCasting(this);
    } else {
      CastHandler.onSpellCastStart(world, (ServerPlayerEntity) user, this);
    }
    return TypedActionResult.consume(user.getStackInHand(hand));
  }

  @Override
  public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
    if (remainingUseTicks == 0) {
      onCastFinished(world, user);
    }
  }

  @Override
  public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    if (remainingUseTicks > 0) {
      onCastFinished(world, user);
    }
  }

  @Override
  public int getMaxUseTime(ItemStack stack) {
    return 20 * 3;
  }

  @Override
  public boolean isUsedOnRelease(ItemStack stack) {
    return true;
  }

  public boolean canHoldSpells() {
    return canHoldSpells;
  }

  private void onCastFinished(World world, LivingEntity user) {
    if (world.isClient()) {
      CastInputHandler.stopCasting();
    } else {
      CastHandler.onSpellCastEnd(world, (ServerPlayerEntity) user, this);
    }
  }
}
