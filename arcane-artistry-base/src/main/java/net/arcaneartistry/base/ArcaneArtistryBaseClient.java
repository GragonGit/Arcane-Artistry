package net.arcaneartistry.base;

import net.arcaneartistry.base.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

/** Registers the one thing base needs client-side: the projectile's renderer. */
public final class ArcaneArtistryBaseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ARCANE_PROJECTILE, FlyingItemEntityRenderer::new);
    }
}
