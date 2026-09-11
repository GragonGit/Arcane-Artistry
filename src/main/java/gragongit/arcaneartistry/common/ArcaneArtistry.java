package gragongit.arcaneartistry.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gragongit.arcaneartistry.common.registry.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class ArcaneArtistry implements ModInitializer {
  public static final String MOD_ID = "arcane-artistry";
  public static final Logger LOGGER = LoggerFactory.getLogger("Arcane Artistry");

  @Override
  public void onInitialize() {
    LOGGER.info("Initializing all Arcane Artistry modules");

    ModRegistries.register();
  }

  public static Identifier id(String path) {
    return Identifier.fromNamespaceAndPath(MOD_ID, path);
  }
}
