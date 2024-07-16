package de.arcane_artistry.spell.spell_effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class DisarmSpellEffect implements SpellEffect {
  private static final double DROP_OFFSET = 0.5;
  private static final double DROP_VELOCITY = 0.2;
  private static final double DROP_CHANCE = 0.3;
  private Hand hand = null;

  public DisarmSpellEffect() {
  }

  public DisarmSpellEffect(Hand hand) {
    this.hand = hand;
  }

  @Override
  public void invoke(World world, Entity owner, Vec3d location) {
    if (owner instanceof LivingEntity entity && world instanceof ServerWorld serverWorld) {
      if (hand == null) {
        dropItem(entity, Hand.MAIN_HAND, serverWorld);
        dropItem(entity, Hand.OFF_HAND, serverWorld);
        return;
      }
      dropItem(entity, hand, serverWorld);
    }
  }

  @Override
  public void invokeOnEntityHitResult(World world, Entity owner, Vec3d location, EntityHitResult entityHitResult) {
    invoke(world, entityHitResult.getEntity(), location);
  }

  private void dropItem(LivingEntity entity, Hand hand, ServerWorld world) {
    ItemStack handStack = entity.getStackInHand(hand);

    if (!handStack.isEmpty()) {
      Vec3d lookVec = entity.getRotationVec(1).normalize();
      Vec3d dropPos = entity.getPos().add(lookVec.x * DROP_OFFSET,
          lookVec.y * DROP_OFFSET + entity.getEyeHeight(entity.getPose()), lookVec.z * DROP_OFFSET);

      ItemEntity itemEntity = new ItemEntity(world, dropPos.x, dropPos.y, dropPos.z, handStack);
      itemEntity.setVelocity(lookVec.x * DROP_VELOCITY, lookVec.y * DROP_VELOCITY, lookVec.z * DROP_VELOCITY);

      if (!(entity instanceof PlayerEntity) && Random.create().nextDouble() > DROP_CHANCE) {
        entity.sendToolBreakStatus(hand);
      } else {
        world.spawnEntity(itemEntity);
      }

      entity.setStackInHand(hand, ItemStack.EMPTY);
    }
  }
}
