package gragongit.arcaneartistry.datagen.staff;

import java.util.concurrent.CompletableFuture;
import gragongit.arcaneartistry.common.registry.ModRegistries;
import gragongit.arcaneartistry.common.staff.StaffDefinition;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

public class StaffProvider extends FabricDynamicRegistryProvider {

  public StaffProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected void configure(HolderLookup.Provider registries, Entries entries) {
    HolderLookup.RegistryLookup<StaffDefinition> lookup = registries.lookupOrThrow(ModRegistries.STAFF_KEY);
    lookup.listElements().forEach(reference -> entries.add(reference.key(), reference.value()));
  }

  @Override
  public String getName() {
    return "Arcane Artistry Staffs";
  }
}
