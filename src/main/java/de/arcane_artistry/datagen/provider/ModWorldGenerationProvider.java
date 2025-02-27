package de.arcane_artistry.datagen.provider;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

public class ModWorldGenerationProvider extends FabricDynamicRegistryProvider {
  public ModWorldGenerationProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
    entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
    entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
  }

  @Override
  public String getName() {
    return "World Gen";
  }
}
