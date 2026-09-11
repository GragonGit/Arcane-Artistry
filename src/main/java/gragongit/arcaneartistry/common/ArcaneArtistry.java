package gragongit.arcaneartistry.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gragongit.arcaneartistry.common.staff.StaffReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

public class ArcaneArtistry implements ModInitializer {
  public static final String MOD_ID = "arcane-artistry";
  public static final Logger LOGGER = LoggerFactory.getLogger("Arcane Artistry");

  @Override
  public void onInitialize() {
    LOGGER.info("Initializing all Arcane Artistry modules");

    ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id(MOD_ID), new StaffReloadListener());
  }

  public static Identifier id(String path) {
    return Identifier.fromNamespaceAndPath(MOD_ID, path);
  }
}
