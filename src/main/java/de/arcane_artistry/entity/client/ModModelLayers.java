package de.arcane_artistry.entity.client;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.entity.ModEntities;
import de.arcane_artistry.entity.client.particle_projectile_entity.ArcaneArtistryProjectileEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModModelLayers {
  public static void registerModModelLayers() {
    ArcaneArtistry.LOGGER.info("Registering Mod Modal Layers for " + ArcaneArtistry.MOD_ID);

    EntityRendererRegistry.register(ModEntities.SPELL_PROJECTILE_ENTITY_TYPE, (context) -> {
      return new ArcaneArtistryProjectileEntityRenderer(context);
    });
  }
}
