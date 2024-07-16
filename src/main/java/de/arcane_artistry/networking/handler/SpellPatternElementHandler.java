package de.arcane_artistry.networking.handler;

import de.arcane_artistry.cast.CastHandler;
import de.arcane_artistry.spell.SpellPatternElement;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpellPatternElementHandler implements PlayChannelHandler {

  @Override
  public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf,
      PacketSender responseSender) {
    SpellPatternElement element = buf.readEnumConstant(SpellPatternElement.class);
    server.execute(() -> {
      CastHandler.receiveSpellPatternElement(player, element);
    });
  }
}
