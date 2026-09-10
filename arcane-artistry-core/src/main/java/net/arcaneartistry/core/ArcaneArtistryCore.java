package net.arcaneartistry.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.arcaneartistry.core.api.GestureCastCallback;
import net.arcaneartistry.core.network.GestureCastC2SPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Entry point for the gesture engine.
 */
public final class ArcaneArtistryCore implements ModInitializer {
  public static final String MOD_ID = "arcane_artistry_core";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    PayloadTypeRegistry.serverboundPlay().register(GestureCastC2SPayload.TYPE, GestureCastC2SPayload.CODEC);

    ServerPlayNetworking.registerGlobalReceiver(GestureCastC2SPayload.TYPE,
        (payload, castContext) -> castContext.server().execute(() -> handleGestureCast(castContext.player(), payload)));

    LOGGER.info("Arcane Artistry core gesture engine ready.");
  }

  private void handleGestureCast(ServerPlayer player, GestureCastC2SPayload payload) {
    ItemStack itemInHand = player.getItemInHand(payload.hand());
    ServerLevel world = player.level();

    GestureCastCallback.GestureCastContext castContext = new GestureCastCallback.GestureCastContext(
        player,
        itemInHand,
        payload.hand(),
        world,
        player.position()
    );

    GestureCastCallback.EVENT.invoker().onGestureCast(castContext);
  }
}
