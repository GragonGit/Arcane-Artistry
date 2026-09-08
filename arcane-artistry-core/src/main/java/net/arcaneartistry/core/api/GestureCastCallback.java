package net.arcaneartistry.core.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * Fired once per completed gesture-cast attempt, carrying "the matched
 * identifier (or no match), the caster, the item stack used, and the
 * world/position context" exactly as specified in design document section
 * 4.1. Listeners should treat an empty {@code matchedId} as "the player
 * finished drawing, but it didn't match a known pattern" --
 * {@code net.arcaneartistry.core.FizzleDefaults} listens for exactly that to
 * play the default fizzle sound (design document open question 4).
 *
 * <p>
 * <b>Why this fires server-side:</b> gesture *drawing* is unavoidably
 * client-only (it's built from raw mouse movement), but whatever a matched
 * identifier ends up meaning -- damage, spawning an entity, granting a
 * potion effect -- needs server authority to actually happen. Rather than
 * trust a client-reported "this is what I matched" result (trivially
 * fakeable by a modified client), core re-derives the match itself,
 * server-side, immediately after receiving the drawn gesture over the
 * network. See {@link net.arcaneartistry.core.network.GestureCastC2SPayload}
 * and {@code ArcaneArtistryCore#handleGestureCast}. This event is the single
 * hook every other module (base's spells, a future alchemy's brews, ...)
 * should listen to.
 */
public interface GestureCastCallback {
  Event<GestureCastCallback> EVENT = EventFactory.createArrayBacked(GestureCastCallback.class,
      listeners -> (player, stack, hand, matchedId, context) -> {
        for (GestureCastCallback listener : listeners) {
          listener.onGestureCast(player, stack, hand, matchedId, context);
        }
      });

  void onGestureCast(ServerPlayer player, ItemStack stack, InteractionHand hand,
      Optional<Identifier> matchedId, GestureCastContext context);

  record GestureCastContext(ServerLevel world, Vec3 position) {
  }
}
