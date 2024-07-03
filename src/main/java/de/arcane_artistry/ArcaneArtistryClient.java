package de.arcane_artistry;

import de.arcane_artistry.callback.ModEvents;
import de.arcane_artistry.entity.client.ModModelLayers;
import de.arcane_artistry.networking.ModNetworkingReceivers;
import de.arcane_artistry.world.tree.ModTrees;
import net.fabricmc.api.ClientModInitializer;

public class ArcaneArtistryClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ModEvents.registerClientEvents();
    ModNetworkingReceivers.registerClientReceivers();
    ModModelLayers.registerModModelLayers();
    ModTrees.registerRenderLayers();
  }
}
