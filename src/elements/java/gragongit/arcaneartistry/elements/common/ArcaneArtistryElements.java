package gragongit.arcaneartistry.elements.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class ArcaneArtistryElements implements ModInitializer {
  public static final String MOD_ID = "arcane-artistry-elements";

  public static final Logger LOGGER = LoggerFactory.getLogger("Arcane Artistry Elements");

  @Override
  public void onInitialize() {
    LOGGER.info("Arcane Artistry Elements!");
  }

  public static Identifier id(String path) {
    return Identifier.fromNamespaceAndPath(MOD_ID, path);
  }
}
