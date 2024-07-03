package de.arcane_artistry.block.custom;

import de.arcane_artistry.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;

public class LapisCrystalBlock extends CornerBlock {

  private final IntProvider experienceDropped;

  public LapisCrystalBlock(IntProvider experienceDropped, Settings settings) {
    super(settings);
    this.experienceDropped = experienceDropped;
  }

  @Override
  public void randomTick(BlockState state, ServerWorld world, BlockPos blockPos, Random random) {
    final int INFUSE_TRIES = 200;
    final int INFUSE_RADIUS = 5;

    BlockPos.iterateRandomly(random, INFUSE_TRIES, blockPos, INFUSE_RADIUS).forEach(pos -> {
      if (world.getBlockState(pos).isIn(BlockTags.SAPLINGS)
          && world.getBlockState(pos).getBlock() != ModBlocks.AZURE_SAPLING) {
        final float INFUSE_VOLUME = 2F;
        final float INFUSE_PITCH = 0.8F + random.nextFloat() * 0.2F;
        final double INFUSE_PARTICLE_OFFSET = 0.5;
        final double INFUSE_PARTICLE_DELTA = 0.2;
        final int INFUSE_PARTICLE_COUNT = 15;
        final double INFUSE_PARTICLE_SPEED = 0;

        world.setBlockState(
            pos,
            ModBlocks.AZURE_SAPLING.getDefaultState());
        world.playSound(null,
            pos,
            SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
            SoundCategory.BLOCKS,
            INFUSE_VOLUME,
            INFUSE_PITCH);
        world.spawnParticles(
            ParticleTypes.HAPPY_VILLAGER,
            pos.getX() + INFUSE_PARTICLE_OFFSET,
            pos.getY() + INFUSE_PARTICLE_OFFSET,
            pos.getZ() + INFUSE_PARTICLE_OFFSET,
            INFUSE_PARTICLE_COUNT,
            INFUSE_PARTICLE_DELTA, INFUSE_PARTICLE_DELTA, INFUSE_PARTICLE_DELTA,
            INFUSE_PARTICLE_SPEED);
      }
    });
  }

  @Override
  public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool,
      boolean dropExperience) {
    if (dropExperience) {
      dropExperienceWhenMined(world, pos, tool, experienceDropped);
    }
  }
}
