package de.arcane_artistry.world.tree;

import java.util.Optional;

import de.arcane_artistry.world.gen.feature.ModConfiguredFeatures;
import net.minecraft.block.SaplingGenerator;

public class ModSaplingGenerators {
  public static final SaplingGenerator AZURE = new SaplingGenerator("azure", 0f, Optional.empty(), Optional.empty(),
      Optional.of(ModConfiguredFeatures.AZURE_KEY), Optional.empty(), Optional.empty(), Optional.empty());
}
