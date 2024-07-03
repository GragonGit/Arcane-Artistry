package de.arcane_artistry.networking;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.networking.handler.SpellPatternElementHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModNetworkingReceivers {

  public static void registerServerReceivers() {
    ArcaneArtistry.LOGGER.info("Registering Receivers for " + ArcaneArtistry.MOD_ID);

    ServerPlayNetworking.registerGlobalReceiver(
        ModNetworkingConstants.SPELL_PATTERN_ELEMENT_PACKET_ID,
        new SpellPatternElementHandler());
  }

  public static void registerClientReceivers() {
    ArcaneArtistry.LOGGER.info("Registering Client Receivers for " + ArcaneArtistry.MOD_ID);
  }
}
