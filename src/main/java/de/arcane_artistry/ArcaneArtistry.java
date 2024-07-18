package de.arcane_artistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.arcane_artistry.block.ModBlocks;
import de.arcane_artistry.callback.ModEvents;
import de.arcane_artistry.entity.ModEntities;
import de.arcane_artistry.item.ModItemGroups;
import de.arcane_artistry.item.ModItems;

import de.arcane_artistry.networking.ModNetworkingReceivers;
import de.arcane_artistry.world.gen.ModWorldGeneration;
import de.arcane_artistry.world.gen.feature.ModFeatures;
import de.arcane_artistry.world.tree.ModTrees;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ArcaneArtistry implements ModInitializer {
  public static final String MOD_ID = "arcane-artistry";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    ModItemGroups.registerModItemGroups();
    ModBlocks.registerModBlocks();
    ModItems.registerModItems();
    ModEvents.registerServerEvents();
    ModNetworkingReceivers.registerServerReceivers();
    ModEntities.registerModEntities();
    ModTrees.registerStrippable();
    ModTrees.registerFlammable();
    ModFeatures.registerFeatures();
    ModWorldGeneration.generateOres();
  }

  public static Identifier newIdentifier(String path) {
    return new Identifier(MOD_ID, path);
  }
}
