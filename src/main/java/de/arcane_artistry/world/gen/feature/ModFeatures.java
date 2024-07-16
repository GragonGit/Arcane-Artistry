package de.arcane_artistry.world.gen.feature;

import de.arcane_artistry.ArcaneArtistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.EmeraldOreFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ModFeatures {
  public static final Feature<EmeraldOreFeatureConfig> LAPIS_CRYSTAL =
      register("lapis_crystal", new LapisCrystalFeature(EmeraldOreFeatureConfig.CODEC));

  private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
    return Registry.register(Registries.FEATURE, ArcaneArtistry.newIdentifier(name), feature);
  }

  public static void registerFeatures() {
    ArcaneArtistry.LOGGER.info("Registering Mod Features");
  }
}
