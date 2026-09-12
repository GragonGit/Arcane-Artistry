package gragongit.arcaneartistry.elements.datagen;

import gragongit.arcaneartistry.common.registry.ModRegistries;
import gragongit.arcaneartistry.common.staff.StaffDefinition;
import gragongit.arcaneartistry.datagen.staff.StaffProvider;
import gragongit.arcaneartistry.elements.common.ArcaneArtistryElements;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;

public class ArcaneArtistryElementsDataGenerator implements DataGeneratorEntrypoint {

  @Override
  public void onInitializeDataGenerator(FabricDataGenerator generator) {
    FabricDataGenerator.Pack pack = generator.createPack();
    pack.addProvider(StaffProvider::new);
  }

  @Override
  public void buildRegistry(RegistrySetBuilder registryBuilder) {
    registryBuilder.add(ModRegistries.STAFF_KEY, ArcaneArtistryElementsDataGenerator::bootstrap);
  }

  private static void bootstrap(BootstrapContext<StaffDefinition> context) {
    context
        .register(ResourceKey.create(ModRegistries.STAFF_KEY, BuiltInRegistries.ITEM.getKey(Items.STICK)),
            new StaffDefinition(ArcaneArtistryElements.FIRE));
  }
}
