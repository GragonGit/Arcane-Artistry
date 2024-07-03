package de.arcane_artistry.world.gen.feature;

import java.util.List;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class ModPlacedFeatures {
  public static final RegistryKey<PlacedFeature> AZURE_PLACED_KEY = registerKey("azure_placed");
  public static final RegistryKey<PlacedFeature> LAPIS_CRYSTAL_PLACED_KEY = registerKey("lapis_crystal_placed");

  public static void bootstrap(Registerable<PlacedFeature> context) {
    RegistryEntryLookup<ConfiguredFeature<?, ?>> registryEntryLookup = context
        .getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

    register(context, AZURE_PLACED_KEY, registryEntryLookup.getOrThrow(ModConfiguredFeatures.AZURE_KEY),
        VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
            RarityFilterPlacementModifier.of(64),
            ModBlocks.AZURE_SAPLING));

    register(context, LAPIS_CRYSTAL_PLACED_KEY, registryEntryLookup.getOrThrow(ModConfiguredFeatures.LAPIS_CRYSTAL_KEY),
        modifiersWithCount(50, HeightRangePlacementModifier.trapezoid(YOffset.fixed(-32), YOffset.fixed(32))));
  }

  public static RegistryKey<PlacedFeature> registerKey(String name) {
    return RegistryKey.of(RegistryKeys.PLACED_FEATURE, ArcaneArtistry.newIdentifier(name));
  }

  public static void register(
      Registerable<PlacedFeature> context,
      RegistryKey<PlacedFeature> key,
      RegistryEntry<ConfiguredFeature<?, ?>> feature,
      List<PlacementModifier> modifiers) {
    context.register(key, new PlacedFeature(feature, List.copyOf(modifiers)));
  }

  private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
    return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
  }

  private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
    return modifiers(CountPlacementModifier.of(count), heightModifier);
  }
}