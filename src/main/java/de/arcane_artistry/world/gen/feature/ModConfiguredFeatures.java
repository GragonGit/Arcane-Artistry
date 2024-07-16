package de.arcane_artistry.world.gen.feature;

import com.google.common.collect.ImmutableList;
import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.block.ModBlocks;
import de.arcane_artistry.world.tree.ModTrees;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.rule.RandomBlockStateMatchRuleTest;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.EmeraldOreFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class ModConfiguredFeatures {
  public static final RegistryKey<ConfiguredFeature<?, ?>> AZURE_KEY = registerKey("azure");
  public static final RegistryKey<ConfiguredFeature<?, ?>> LAPIS_CRYSTAL_KEY = registerKey("lapis_crystal");

  public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
    register(context, AZURE_KEY, Feature.TREE, ModTrees.AZURE_TREE_FEATURE_CONFIG);

    register(context, LAPIS_CRYSTAL_KEY, ModFeatures.LAPIS_CRYSTAL,
        new EmeraldOreFeatureConfig(ImmutableList.of(
            OreFeatureConfig.createTarget(
                new RandomBlockStateMatchRuleTest(Blocks.STONE.getDefaultState(), 1F),
                ModBlocks.STONE_LAPIS_CRYSTAL.getDefaultState()),
            OreFeatureConfig.createTarget(
                new RandomBlockStateMatchRuleTest(Blocks.DEEPSLATE.getDefaultState(), 1F),
                ModBlocks.DEEPSLATE_LAPIS_CRYSTAL.getDefaultState()))));
  }

  public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
    return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, ArcaneArtistry.newIdentifier(name));
  }

  private static <C extends FeatureConfig, F extends Feature<C>> void register(
      Registerable<ConfiguredFeature<?, ?>> context,
      RegistryKey<ConfiguredFeature<?, ?>> key,
      F feature,
      C configuration) {
    context.register(key, new ConfiguredFeature<>(feature, configuration));
  }
}