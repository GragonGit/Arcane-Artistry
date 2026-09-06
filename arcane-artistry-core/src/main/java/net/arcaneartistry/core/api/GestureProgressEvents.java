package net.arcaneartistry.core.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Client-only, purely informational events describing an in-progress gesture
 * draw. Core fires these but renders nothing itself.
 *
 * <p>This directly answers design document open question 3 ("is it possible
 * to make [the gesture trail] hidden? Adding the functionality but no
 * rendering so that modules can just add the rendering for the given
 * information"): subscribe to these from any module -- or a purely
 * cosmetic/client-side addon -- to draw your own trail, screen distortion,
 * particle line, controller rumble, whatever. Core ships zero built-in
 * renderer for them.
 */
public final class GestureProgressEvents {
    private GestureProgressEvents() {
    }

    public interface Started {
        void onGestureStarted(ClientPlayerEntity player, ItemStack stack);
    }

    public interface StrokeAdded {
        void onGestureStrokeAdded(ClientPlayerEntity player, List<GestureDirection> strokesSoFar);
    }

    public interface Ended {
        void onGestureEnded(ClientPlayerEntity player, List<GestureDirection> finalStrokes);
    }

    public static final Event<Started> STARTED = EventFactory.createArrayBacked(Started.class,
            listeners -> (player, stack) -> {
                for (Started listener : listeners) {
                    listener.onGestureStarted(player, stack);
                }
            });

    public static final Event<StrokeAdded> STROKE_ADDED = EventFactory.createArrayBacked(StrokeAdded.class,
            listeners -> (player, strokes) -> {
                for (StrokeAdded listener : listeners) {
                    listener.onGestureStrokeAdded(player, strokes);
                }
            });

    public static final Event<Ended> ENDED = EventFactory.createArrayBacked(Ended.class,
            listeners -> (player, strokes) -> {
                for (Ended listener : listeners) {
                    listener.onGestureEnded(player, strokes);
                }
            });
}
