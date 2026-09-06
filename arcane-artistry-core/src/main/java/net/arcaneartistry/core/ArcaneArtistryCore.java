package net.arcaneartistry.core;

import net.arcaneartistry.core.api.GestureCastCallback;
import net.arcaneartistry.core.api.GesturePatternRegistry;
import net.arcaneartistry.core.network.GestureCastC2SPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
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
        PayloadTypeRegistry.playC2S().register(GestureCastC2SPayload.ID, GestureCastC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(GestureCastC2SPayload.ID, (payload, context) ->
                context.server().execute(() -> handleGestureCast(context.player(), payload)));

        FizzleDefaults.register();

        LOGGER.info("Arcane Artistry core gesture engine ready.");
    }

    private void handleGestureCast(ServerPlayerEntity player, GestureCastC2SPayload payload) {
        var stack = player.getStackInHand(payload.hand());
        Optional<Identifier> matched = GesturePatternRegistry.match(payload.gesture());

        ServerWorld world = player.getServerWorld();
        var context = new GestureCastCallback.GestureCastContext(world, player.getPos());

        GestureCastCallback.EVENT.invoker().onGestureCast(player, stack, payload.hand(), matched, context);
    }
}
