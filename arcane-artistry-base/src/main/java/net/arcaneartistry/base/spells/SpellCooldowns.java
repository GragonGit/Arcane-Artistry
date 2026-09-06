package net.arcaneartistry.base.spells;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-spell cooldowns, independent of whichever staff cast them -- a staff
 * also has its own cooldown (its own {@code cooldown_ticks}, design document
 * section 5), applied separately via vanilla's
 * {@code PlayerEntity#getItemCooldownManager()} directly on the item.
 *
 * <p>Deliberately simple and in-memory for v1: it resets on server restart
 * or a relog rather than persisting to player data. That's an acceptable v1
 * limitation given the design document's mana/resource system (section 9),
 * which will likely subsume ad-hoc cooldown tracking like this outright, is
 * explicitly future work.
 */
public final class SpellCooldowns {
    private static final Map<UUID, Map<Identifier, Long>> READY_AT_TICK = new ConcurrentHashMap<>();

    private SpellCooldowns() {
    }

    public static boolean isReady(ServerPlayerEntity player, Identifier spellId) {
        Long readyAtTick = READY_AT_TICK.getOrDefault(player.getUuid(), Map.of()).get(spellId);
        return readyAtTick == null || player.getWorld().getTime() >= readyAtTick;
    }

    public static void trigger(ServerPlayerEntity player, Identifier spellId, int cooldownTicks) {
        if (cooldownTicks <= 0) {
            return;
        }
        READY_AT_TICK
                .computeIfAbsent(player.getUuid(), uuid -> new ConcurrentHashMap<>())
                .put(spellId, player.getWorld().getTime() + cooldownTicks);
    }
}
