package de.arcane_artistry.world.tree;

import java.util.OptionalInt;

import de.arcane_artistry.block.ModBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class ModTrees {
  public static final TreeFeatureConfig AZURE_TREE_FEATURE_CONFIG = new TreeFeatureConfig.Builder(
      BlockStateProvider.of(ModBlocks.AZURE_LOG),
      new StraightTrunkPlacer(4, 2, 0),
      new WeightedBlockStateProvider(DataPool.<BlockState>builder()
          .add(ModBlocks.AZURE_LEAVES.getDefaultState(), 7)
          .add(ModBlocks.SHIMMERING_AZURE_LEAVES.getDefaultState(), 1).build()),
      new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(1), 4),
      new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))).ignoreVines().build();

  public static void registerStrippable() {
    StrippableBlockRegistry.register(ModBlocks.AZURE_LOG, ModBlocks.STRIPPED_AZURE_LOG);
    StrippableBlockRegistry.register(ModBlocks.AZURE_WOOD, ModBlocks.STRIPPED_AZURE_WOOD);
  }

  public static void registerFlammable() {
    FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.AZURE_LOG, 5, 5);
    FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.AZURE_WOOD, 5, 5);
    FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_AZURE_LOG, 5, 5);
    FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_AZURE_WOOD, 5, 5);
    FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.AZURE_PLANKS, 5, 20);
    FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.AZURE_LEAVES, 30, 60);
    FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.SHIMMERING_AZURE_LEAVES, 30, 60);
  }

  public static void registerRenderLayers() {
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AZURE_LEAVES, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHIMMERING_AZURE_LEAVES, RenderLayer.getCutout());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AZURE_SAPLING, RenderLayer.getCutout());
  }
}
