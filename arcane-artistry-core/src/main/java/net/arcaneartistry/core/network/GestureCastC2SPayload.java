package net.arcaneartistry.core.network;

import net.arcaneartistry.core.api.GestureDirection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;

import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Client -> server: "I finished drawing this gesture with this hand."
 * The server re-derives the match itself against its own authoritative
 * {@code GesturePatternRegistry} rather than trusting a client-supplied
 * result -- see {@link net.arcaneartistry.core.api.GestureCastCallback}.
 *
 * <p>
 * Built against the {@code CustomPacketPayload}/{@code StreamCodec}
 * networking API introduced in the 1.20.5-era packet rework, using Mojang's
 * official names (this codebase targets 26.2, which ships unobfuscated).
 */
public record GestureCastC2SPayload(InteractionHand hand, List<GestureDirection> gesture)
    implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<GestureCastC2SPayload> TYPE =
      new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("arcane_artistry_core", "gesture_cast"));

  private static final StreamCodec<ByteBuf, InteractionHand> HAND_CODEC = ByteBufCodecs.BYTE.map(
      b -> InteractionHand.values()[b], hand -> (byte) hand.ordinal());

  // NOTE: .apply(ByteBufCodecs.list()) mirrors the CodecOperation-based
  // list-collection helper Mojang's StreamCodec exposes; if this specific
  // line doesn't compile against your exact Fabric API build, check
  // ByteBufCodecs' generated sources for the current list-collector name.
  public static final StreamCodec<RegistryFriendlyByteBuf, GestureCastC2SPayload> CODEC = StreamCodec.composite(
      HAND_CODEC, GestureCastC2SPayload::hand,
      GestureDirection.packetCodec().apply(ByteBufCodecs.list()), GestureCastC2SPayload::gesture,
      GestureCastC2SPayload::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
