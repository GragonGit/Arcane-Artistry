package de.arcane_artistry.world.gen.feature;

import com.mojang.serialization.Codec;
import de.arcane_artistry.block.custom.CornerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.EmeraldOreFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class LapisCrystalFeature extends Feature<EmeraldOreFeatureConfig> {
  public LapisCrystalFeature(Codec<EmeraldOreFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public boolean generate(FeatureContext<EmeraldOreFeatureConfig> context) {
    StructureWorldAccess structureWorldAccess = context.getWorld();
    BlockPos blockPos = context.getOrigin();
    EmeraldOreFeatureConfig emeraldOreFeatureConfig = context.getConfig();
    for (OreFeatureConfig.Target target : emeraldOreFeatureConfig.targets) {
      if (!target.target.test(structureWorldAccess.getBlockState(blockPos), context.getRandom()))
        continue;

      Boolean[] isSideAir = calculateAirSides(structureWorldAccess, blockPos);
      if (!hasAirCorner(isSideAir))
        continue;

      structureWorldAccess.setBlockState(
          blockPos,
          calculateCornerState(isSideAir, target.state),
          Block.NOTIFY_LISTENERS);
      break;
    }
    return true;
  }

  private static Boolean[] calculateAirSides(StructureWorldAccess world, BlockPos blockPos) {
    Boolean[] isSideAir = {
        world.getBlockState(blockPos.up()).isAir(),
        world.getBlockState(blockPos.down()).isAir(),
        world.getBlockState(blockPos.north()).isAir(),
        world.getBlockState(blockPos.east()).isAir(),
        world.getBlockState(blockPos.south()).isAir(),
        world.getBlockState(blockPos.west()).isAir()
    };
    return isSideAir;
  }

  private static boolean hasAirCorner(Boolean[] isSideAir) {
    if (isSideAir[0] && isSideAir[2] && isSideAir[3])
      return true;
    if (isSideAir[0] && isSideAir[3] && isSideAir[4])
      return true;
    if (isSideAir[0] && isSideAir[4] && isSideAir[5])
      return true;
    if (isSideAir[0] && isSideAir[5] && isSideAir[2])
      return true;
    if (isSideAir[1] && isSideAir[2] && isSideAir[3])
      return true;
    if (isSideAir[1] && isSideAir[3] && isSideAir[4])
      return true;
    if (isSideAir[1] && isSideAir[4] && isSideAir[5])
      return true;
    if (isSideAir[1] && isSideAir[5] && isSideAir[2])
      return true;
    return false;
  }

  private static BlockState calculateCornerState(Boolean[] isSideAir, BlockState blockState) {
    return blockState
        .with(CornerBlock.UP, isSideAir[0])
        .with(CornerBlock.NORTH, isSideAir[2])
        .with(CornerBlock.EAST, isSideAir[3]);
  }
}
