package de.arcane_artistry.spell.spell_effect;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface SpellEffect {
  void invoke(World world, Entity owner, Vec3d location);

  default void invokeOnHitResult(World world, Entity owner, Vec3d location, HitResult hitResult) {
    invoke(world, owner, location);
  }

  default void invokeOnBlockHitResult(World world, Entity owner, Vec3d location,
      BlockHitResult blockHitResult) {
    invoke(world, owner, location);
  }

  default void invokeOnBlockState(World world, Entity owner, Vec3d location, BlockState blockState) {
    invoke(world, owner, location);
  }

  default void invokeOnEntityHitResult(World world, Entity owner, Vec3d location,
      EntityHitResult entityHitResult) {
    invoke(world, owner, location);
  }

  default void invokeOnPlayerEntity(World world, Entity owner, Vec3d location, PlayerEntity playerEntity) {
    invoke(world, owner, location);
  }
}
