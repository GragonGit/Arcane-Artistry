package net.arcaneartistry.core.network;

import net.arcaneartistry.core.api.GestureDirection;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * Client -> server: "I finished drawing this gesture with this hand."
 * The server re-derives the match itself against its own authoritative
 * {@code GesturePatternRegistry} rather than trusting a client-supplied
 * result -- see {@link net.arcaneartistry.core.api.GestureCastCallback}.
 *
 * <p>Built against the {@code CustomPayload}/{@code PacketCodec} networking
 * API (the 1.20.5-era packet rework). This corner of the Fabric API has
 * moved a couple of times before; if your Fabric API version's codec
 * combinator names differ slightly, this is the first file to check.
 */
public record GestureCastC2SPayload(Hand hand, List<GestureDirection> gesture) implements CustomPayload {
    public static final Id<GestureCastC2SPayload> ID =
            new Id<>(Identifier.of("arcane_artistry_core", "gesture_cast"));

    private static final PacketCodec<RegistryByteBuf, Hand> HAND_CODEC =
            PacketCodecs.indexed(id -> Hand.values()[id], Hand::ordinal);

    public static final PacketCodec<RegistryByteBuf, GestureCastC2SPayload> CODEC = PacketCodec.tuple(
            HAND_CODEC, GestureCastC2SPayload::hand,
            GestureDirection.<RegistryByteBuf>packetCodec().collect(PacketCodecs.toList()), GestureCastC2SPayload::gesture,
            GestureCastC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
