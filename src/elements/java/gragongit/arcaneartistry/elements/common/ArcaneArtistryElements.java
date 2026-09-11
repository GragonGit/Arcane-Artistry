package gragongit.arcaneartistry.elements.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gragongit.arcaneartistry.common.ArcaneArtistry;
import gragongit.arcaneartistry.common.registry.ModRegistries;
import gragongit.arcaneartistry.common.staff.StaffType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public class ArcaneArtistryElements implements ModInitializer {
  public static final String MOD_ID = ArcaneArtistry.MOD_ID + "-elements";

  public static final Logger LOGGER = LoggerFactory.getLogger("Arcane Artistry Elements");

  public static final StaffType FIRE = Registry.register(ModRegistries.STAFF_TYPES, ArcaneArtistryElements.id("fire"), new StaffType());

  @Override
  public void onInitialize() {
    LOGGER.info("Arcane Artistry Elements!");
  }

  public static Identifier id(String path) {
    return Identifier.fromNamespaceAndPath(MOD_ID, path);
  }
}
