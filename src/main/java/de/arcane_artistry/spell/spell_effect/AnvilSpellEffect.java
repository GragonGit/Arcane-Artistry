package de.arcane_artistry.spell.spell_effect;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AnvilSpellEffect implements SpellEffect {
  private final int maxSpawnHeight;

  public AnvilSpellEffect(int maxSpawnHeight) {
    this.maxSpawnHeight = maxSpawnHeight;
  }

  @Override
  public void invoke(World world, Entity owner, Vec3d location) {
    BlockPos blockPos = BlockPos.ofFloored(location);
    world.setBlockState(blockPos, Blocks.DAMAGED_ANVIL.getDefaultState());
  }

  @Override
  public void invokeOnHitResult(World world, Entity owner, Vec3d location, HitResult hitResult) {
    if (hitResult.getType().equals(HitResult.Type.ENTITY)) {
      Vec3d hitResultPostion = hitResult.getPos();
      for (int i = 1; i <= maxSpawnHeight
          && world.getBlockState(BlockPos.ofFloored(hitResultPostion.add(0, i, 0))).isAir(); i++) {
        location = hitResultPostion.add(0, i, 0);
      }
    }
    invoke(world, owner, location);
  }
}
