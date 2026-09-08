package net.arcaneartistry.base;

import net.arcaneartistry.base.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

/**
 * Registers the one thing base needs client-side: the projectile's renderer.
 *
 * <p>
 * NOTE: {@code ThrownItemRenderer} is the Mojang-mapped name for what Yarn
 * called {@code FlyingItemEntityRenderer} (the "renders as a spinning item"
 * renderer vanilla uses for snowballs/eggs/ender pearls). Worth confirming
 * against generated sources if this doesn't resolve on your exact build.
 */
public final class ArcaneArtistryBaseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ARCANE_PROJECTILE, ThrownItemRenderer::new);
    }
}
