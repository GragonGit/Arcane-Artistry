package net.arcaneartistry.core;

import net.arcaneartistry.core.api.GestureCastCallback;
import net.arcaneartistry.core.api.GesturePatternRegistry;
import net.arcaneartistry.core.network.GestureCastC2SPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Entry point for the gesture engine (design document section 4). Registers
 * networking, wires the one server-authoritative match-and-fire step, and
 * turns on the default fizzle feedback.
 */
public final class ArcaneArtistryCore implements ModInitializer {
  public static final String MOD_ID = "arcane_artistry_core";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    PayloadTypeRegistry.serverboundPlay().register(GestureCastC2SPayload.TYPE, GestureCastC2SPayload.CODEC);

    ServerPlayNetworking.registerGlobalReceiver(GestureCastC2SPayload.TYPE,
        (payload, context) -> context.server().execute(() -> handleGestureCast(context.player(), payload)));

    FizzleDefaults.register();

    LOGGER.info("Arcane Artistry core gesture engine ready.");
  }

  private void handleGestureCast(ServerPlayer player, GestureCastC2SPayload payload) {
    var stack = player.getItemInHand(payload.hand());
    Optional<Identifier> matched = GesturePatternRegistry.match(payload.gesture());

    ServerLevel world = player.level();
    var context = new GestureCastCallback.GestureCastContext(world, player.position());

    GestureCastCallback.EVENT.invoker().onGestureCast(player, stack, payload.hand(), matched, context);
  }
}
