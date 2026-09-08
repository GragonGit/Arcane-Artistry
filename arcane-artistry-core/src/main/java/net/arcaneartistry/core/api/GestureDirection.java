package net.arcaneartistry.core.api;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import io.netty.buffer.ByteBuf;

/**
 * The four cardinal directions a gesture stroke can be drawn in.
 *
 * <p>
 * Deliberately a separate type from {@code net.minecraft.core.Direction}
 * (the six 3D block/facing directions) -- this enum is purely a 2D,
 * screen-space mouse-drag direction used for gesture recognition and has
 * nothing to do with the game world.
 *
 * <p>
 * Cardinal-only (4-direction) is a deliberate v1 choice recorded in the
 * design document's open questions: reliable recognition with mouse input
 * was prioritized over the larger pattern-space an 8-direction (diagonal
 * inclusive) scheme would allow. Every pattern in this codebase is typed as
 * plain {@code List<GestureDirection>} rather than something more rigid, so
 * extending this enum later (diagonals, or decomposing shapes into chains of
 * directions, both explicitly floated in the design document) does not
 * require restructuring the registry, the network payload, or callers.
 */
public enum GestureDirection {
  UP,
  DOWN,
  LEFT,
  RIGHT;

  /**
   * Keyed to the common {@code ByteBuf} supertype rather than a generic
   * {@code <B extends ByteBuf>} parameter: {@code StreamCodec} combinators
   * such as {@code StreamCodec.composite} accept a
   * {@code StreamCodec<? super B, T>} for each component, so a codec typed
   * to plain {@code ByteBuf} already satisfies that no matter which more
   * specific buffer type (e.g. {@code RegistryFriendlyByteBuf}) the
   * containing packet actually uses.
   */
  public static StreamCodec<ByteBuf, GestureDirection> packetCodec() {
    return ByteBufCodecs.BYTE.map(
        b -> GestureDirection.values()[b],
        direction -> (byte) direction.ordinal());
  }
}
