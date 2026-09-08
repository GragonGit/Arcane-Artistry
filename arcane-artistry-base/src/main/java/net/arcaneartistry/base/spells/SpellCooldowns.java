package net.arcaneartistry.base.spells;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-spell cooldowns, independent of whichever staff cast them -- a staff
 * also has its own cooldown (its own {@code cooldown_ticks}, design document
 * section 5), applied separately via vanilla's
 * {@code Player#getCooldowns()} directly on the item.
 *
 * <p>
 * Deliberately simple and in-memory for v1: it resets on server restart
 * or a relog rather than persisting to player data. That's an acceptable v1
 * limitation given the design document's mana/resource system (section 9),
 * which will likely subsume ad-hoc cooldown tracking like this outright, is
 * explicitly future work.
 */
public final class SpellCooldowns {
  private static final Map<UUID, Map<Identifier, Long>> READY_AT_TICK = new ConcurrentHashMap<>();

  private SpellCooldowns() {
  }

  public static boolean isReady(ServerPlayer player, Identifier spellId) {
    Long readyAtTick = READY_AT_TICK.getOrDefault(player.getUUID(), Map.of()).get(spellId);
    return readyAtTick == null || player.level().getGameTime() >= readyAtTick;
  }

  public static void trigger(ServerPlayer player, Identifier spellId, int cooldownTicks) {
    if (cooldownTicks <= 0) {
      return;
    }
    READY_AT_TICK
        .computeIfAbsent(player.getUUID(), uuid -> new ConcurrentHashMap<>())
        .put(spellId, player.level().getGameTime() + cooldownTicks);
  }
}
