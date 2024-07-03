package de.arcane_artistry.callback;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.callback.cast.CastEndCallback;
import de.arcane_artistry.callback.mouse.client.MouseInputCallback;
import de.arcane_artistry.cast.CastHandler;
import de.arcane_artistry.cast.GlideHandler;
import de.arcane_artistry.cast.client.CastInputHandler;
import de.arcane_artistry.spell.SpellHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;

public class ModEvents {
  public static void registerServerEvents() {
    ArcaneArtistry.LOGGER.info("Registering Server Events for " + ArcaneArtistry.MOD_ID);

    CastEndCallback.EVENT.register(SpellHandler::handleSpellCast);

    ServerTickEvents.END_SERVER_TICK
        .register(server -> server.getPlayerManager().getPlayerList().forEach(GlideHandler::checkPlayerGlidingTick));
    EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
      if (entity instanceof PlayerEntity player)
        return GlideHandler.isPlayerGliding(player);
      return false;
    });
    ServerTickEvents.END_SERVER_TICK
        .register(client -> client.getPlayerManager().getPlayerList().forEach(CastHandler::tickHoldingSpell));
  }

  public static void registerClientEvents() {
    ArcaneArtistry.LOGGER.info("Registering Client Events for " + ArcaneArtistry.MOD_ID);

    MouseInputCallback.EVENT.register(CastInputHandler::handleMouseDelta);
    ClientTickEvents.START_CLIENT_TICK
        .register(client -> CastInputHandler.onPlayerTick(MinecraftClient.getInstance().player));
  }
}
