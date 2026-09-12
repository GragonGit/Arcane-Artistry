package gragongit.arcaneartistry.datagen;

import gragongit.arcaneartistry.common.registry.ModRegistries;
import gragongit.arcaneartistry.common.staff.StaffDefinition;
import gragongit.arcaneartistry.datagen.staff.StaffProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.worldgen.BootstrapContext;

public class ArcaneArtistryDataGenerator implements DataGeneratorEntrypoint {

  @Override
  public void onInitializeDataGenerator(FabricDataGenerator generator) {
    FabricDataGenerator.Pack pack = generator.createPack();
    pack.addProvider(StaffProvider::new);
  }

  @Override
  public void buildRegistry(RegistrySetBuilder registryBuilder) {
    registryBuilder.add(ModRegistries.STAFF_KEY, ArcaneArtistryDataGenerator::bootstrap);
  }

  private static void bootstrap(BootstrapContext<StaffDefinition> context) {}
}
