package de.arcane_artistry.world.gen;

import de.arcane_artistry.world.gen.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.gen.GenerationStep;

public class ModWorldGeneration {
  public static void generateOres() {
    BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_FOREST), GenerationStep.Feature.VEGETAL_DECORATION,
        ModPlacedFeatures.AZURE_PLACED_KEY);

    BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
        ModPlacedFeatures.LAPIS_CRYSTAL_PLACED_KEY);
  }
}
