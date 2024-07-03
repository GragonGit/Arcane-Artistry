package de.arcane_artistry.datagen;

import de.arcane_artistry.datagen.provider.ModBlockTagProvider;
import de.arcane_artistry.datagen.provider.ModItemTagProvider;
import de.arcane_artistry.datagen.provider.ModLootTableProvider;
import de.arcane_artistry.datagen.provider.ModModelProvider;
import de.arcane_artistry.datagen.provider.ModRecipeProvider;
import de.arcane_artistry.datagen.provider.ModWorldGenerationProvider;
import de.arcane_artistry.datagen.provider.lang.ModEnglishLangProvider;
import de.arcane_artistry.world.gen.feature.ModConfiguredFeatures;
import de.arcane_artistry.world.gen.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class ModDataGenerator implements DataGeneratorEntrypoint {
  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

    ModBlockTagProvider blockTagProvider = pack.addProvider(ModBlockTagProvider::new);
    pack.addProvider((out, future) -> new ModItemTagProvider(out, future, blockTagProvider));
    pack.addProvider(ModLootTableProvider::new);
    pack.addProvider(ModModelProvider::new);
    pack.addProvider(ModRecipeProvider::new);
    pack.addProvider(ModWorldGenerationProvider::new);
    pack.addProvider(ModEnglishLangProvider::new);
  }

  @Override
  public void buildRegistry(RegistryBuilder registryBuilder) {
    registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
    registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
  }
}
