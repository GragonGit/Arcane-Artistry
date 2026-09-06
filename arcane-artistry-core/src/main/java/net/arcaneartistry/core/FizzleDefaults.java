package net.arcaneartistry.core;

import net.arcaneartistry.core.api.GestureCastCallback;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default "nothing happened" feedback for a gesture that didn't match
 * anything -- design document open question 4 ("should core define a
 * generic fizzle effect/sound that addons can opt out of? Yes").
 *
 * <p>This is a plain opt-out, not a cancellable event: v1 doesn't need
 * anything more elaborate than that. Call {@link #setEnabled(boolean)} once,
 * from your own mod initializer, if you want to fully replace this with
 * your own feedback.
 *
 * <p>{@link #playAt} is public so other modules can reuse the exact same cue
 * for their *own* "a gesture was drawn, but wasn't actually allowed to cast"
 * cases (wrong staff for this spell, on cooldown, ...) without duplicating
 * sound/particle choices in every module -- see how the base module's
 * gesture-cast listener uses it.
 */
public final class FizzleDefaults {
    private static final AtomicBoolean ENABLED = new AtomicBoolean(true);

    private FizzleDefaults() {
    }

    public static void setEnabled(boolean enabled) {
        ENABLED.set(enabled);
    }

    public static void register() {
        GestureCastCallback.EVENT.register((player, stack, hand, matchedId, context) -> {
            if (matchedId.isEmpty() && ENABLED.get()) {
                playAt(context.world(), context.position());
            }
        });
    }

    public static void playAt(ServerWorld world, Vec3d position) {
        world.playSound(null, position.x, position.y, position.z,
                SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5f, 1.6f);
    }
}
