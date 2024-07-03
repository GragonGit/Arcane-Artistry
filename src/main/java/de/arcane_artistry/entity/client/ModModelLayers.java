package de.arcane_artistry.entity.client;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.entity.ModEntities;
import de.arcane_artistry.entity.client.BallProjectileEntity.BallProjectileEntityModel;
import de.arcane_artistry.entity.client.BallProjectileEntity.BallProjectileEntityRenderer;
import de.arcane_artistry.entity.client.ParticleProjectileEntity.ArcaneArtistryProjectileEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
  public static final EntityModelLayer BALL_PROJECTILE_LAYER = new EntityModelLayer(
      new Identifier(ArcaneArtistry.MOD_ID, "ball_projectile"), "main");

  public static void registerModModelLayers() {
    ArcaneArtistry.LOGGER.info("Registering Mod Modal Layers for " + ArcaneArtistry.MOD_ID);

    EntityRendererRegistry.register(ModEntities.BALL_PROJECTILE_ENTITY_TYPE, (context) -> {
      return new BallProjectileEntityRenderer(context);
    });
    EntityModelLayerRegistry.registerModelLayer(BALL_PROJECTILE_LAYER, BallProjectileEntityModel::getTexturedModelData);

    EntityRendererRegistry.register(ModEntities.SPELL_PROJECTILE_ENTITY_TYPE, (context) -> {
      return new ArcaneArtistryProjectileEntityRenderer(context);
    });
  }
}
