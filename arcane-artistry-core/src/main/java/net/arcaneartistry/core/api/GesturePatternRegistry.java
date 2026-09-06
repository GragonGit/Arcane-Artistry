package net.arcaneartistry.core.api;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maps a drawn gesture (an exact, ordered list of {@link GestureDirection}
 * strokes) to the {@link Identifier} of whatever registered it (design
 * document section 4.1/4.3).
 *
 * <p>Core has zero opinion on what that identifier <em>means</em> -- it might
 * be a spell (arcane_artistry_base), a brew (a future arcane_artistry_alchemy),
 * or anything else. Consumers register their own patterns and are
 * responsible for un-registering them (see {@link #unregister}) if their
 * backing content is removed or changed on a datapack {@code /reload}. The
 * registry itself has no idea when a reload happens or which entries
 * "belong" to which module, so it cannot safely clear entries on your
 * behalf -- see {@code SpellDefinitionLoader} in the base module for the
 * pattern a reload listener should follow (track what you registered last
 * time, unregister exactly that, then register the fresh set).
 *
 * <p>The registry is expected to stay small (dozens of patterns, not
 * thousands -- design document section 4.2 step 4), so an exact-match lookup
 * via a hash map is sufficient. No fuzzy/nearest-match logic is implemented
 * for v1.
 */
public final class GesturePatternRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger("arcane_artistry_core/gestures");

    private static final Map<List<GestureDirection>, Identifier> PATTERN_TO_ID = new ConcurrentHashMap<>();
    private static final Map<Identifier, List<GestureDirection>> ID_TO_PATTERN = new ConcurrentHashMap<>();

    private GesturePatternRegistry() {
    }

    /**
     * Registers {@code pattern} as triggering {@code id} when drawn exactly.
     * If {@code id} was already registered, its old pattern is replaced.
     * If {@code pattern} is already claimed by a *different* id, the new
     * registration wins and a warning is logged: two modules defining the
     * same gesture is a content conflict worth knowing about, but not one
     * core should crash over, since that would take every module in the jar
     * down with it.
     */
    public static synchronized void register(Identifier id, List<GestureDirection> pattern) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(pattern, "pattern");
        if (pattern.isEmpty()) {
            LOGGER.warn("Refusing to register an empty gesture pattern for {}", id);
            return;
        }
        List<GestureDirection> immutablePattern = List.copyOf(pattern);

        List<GestureDirection> previousPatternForId = ID_TO_PATTERN.put(id, immutablePattern);
        if (previousPatternForId != null) {
            PATTERN_TO_ID.remove(previousPatternForId);
        }

        Identifier existingOwner = PATTERN_TO_ID.put(immutablePattern, id);
        if (existingOwner != null && !existingOwner.equals(id)) {
            LOGGER.warn("Gesture pattern {} is claimed by both {} and {}; {} will now match.",
                    immutablePattern, existingOwner, id, id);
        }
    }

    /** Removes {@code id}'s pattern, if any. Safe to call even if it was never registered. */
    public static synchronized void unregister(Identifier id) {
        List<GestureDirection> pattern = ID_TO_PATTERN.remove(id);
        if (pattern != null) {
            PATTERN_TO_ID.remove(pattern, id);
        }
    }

    /** Convenience for reload listeners replacing a whole previously-owned batch at once. */
    public static synchronized void unregisterAll(Collection<Identifier> ids) {
        ids.forEach(GesturePatternRegistry::unregister);
    }

    /** Exact-match lookup. Empty if nothing registered matches the drawn gesture. */
    public static Optional<Identifier> match(List<GestureDirection> drawnGesture) {
        return Optional.ofNullable(PATTERN_TO_ID.get(drawnGesture));
    }
}
