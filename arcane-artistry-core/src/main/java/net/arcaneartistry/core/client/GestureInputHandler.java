package net.arcaneartistry.core.client;

import net.arcaneartistry.core.api.GestureDirection;
import net.arcaneartistry.core.api.GestureProgressEvents;
import net.arcaneartistry.core.network.GestureCastC2SPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-side gesture capture state machine (design document section 4.2).
 *
 * <p>
 * <b>Deliberately implemented without a mixin into raw mouse input.</b>
 * Instead, every client tick this reads how far the player's look direction
 * moved *this tick* -- which mouse-look has already turned it by by the time
 * we look, via completely ordinary, long-stable {@code Entity} yaw/pitch
 * accessors -- buckets that movement into a stroke direction once it passes
 * a threshold, and then resets yaw/pitch back to where they were at the
 * start of the tick.
 *
 * <p>
 * That reset both "locks the camera" (no net rotation reaches the
 * renderer) and lands exactly on next tick's interpolation baseline, so
 * there's no visual snap/jitter: {@code prevYaw}/{@code prevPitch} are
 * ordinarily copied from the current yaw/pitch at the start of the next
 * tick, and since we've just set yaw/pitch back to last tick's locked value,
 * that's what gets copied forward too.
 *
 * <p>
 * This trades a small amount of directness for relying only on APIs
 * ({@code Entity#getYRot/setYRot/getXRot/setXRot}, Fabric's
 * {@code ClientTickEvents}) that have been stable for a very long time,
 * rather than a private {@code Mouse} field/method whose exact name is
 * exactly the kind of detail that's most likely to have shifted in a
 * Minecraft version this project was written without direct access to.
 */
public final class GestureInputHandler {
  /** Degrees of look-rotation that count as one stroke. Tune to taste. */
  private static final double DIRECTION_THRESHOLD_DEGREES = 10.0;

  private boolean casting = false;
  private float lockedYaw;
  private float lockedPitch;
  private double accumulatedYaw;
  private double accumulatedPitch;
  private final List<GestureDirection> strokes = new ArrayList<>();

  public boolean isCasting() {
    return casting;
  }

  public void startCast(LocalPlayer player, ItemStack stack) {
    casting = true;
    lockedYaw = player.getYRot();
    lockedPitch = player.getXRot();
    accumulatedYaw = 0;
    accumulatedPitch = 0;
    strokes.clear();
    GestureProgressEvents.STARTED.invoker().onGestureStarted(player, stack);
  }

  public void endCast(LocalPlayer player, ItemStack stack, InteractionHand hand) {
    if (!casting) {
      return;
    }
    casting = false;
    List<GestureDirection> finalGesture = List.copyOf(strokes);
    GestureProgressEvents.ENDED.invoker().onGestureEnded(player, finalGesture);
    ArcaneArtistryCoreClient.sendCast(new GestureCastC2SPayload(hand, finalGesture));
  }

  /** Safety net so a cast can never get "stuck" across a disconnect/rejoin. */
  public void cancel() {
    casting = false;
    strokes.clear();
  }

  void onEndClientTick(Minecraft client) {
    if (!casting || client.player == null) {
      return;
    }
    LocalPlayer player = client.player;

    double deltaYaw = Mth.wrapDegrees(player.getYRot() - lockedYaw);
    double deltaPitch = player.getXRot() - lockedPitch;

    // Cancel this tick's rotation: this both "locks" the camera and
    // leaves no net delta for the renderer's interpolation to pick up.
    player.setYRot(lockedYaw);
    player.setXRot(lockedPitch);

    accumulatedYaw += deltaYaw;
    accumulatedPitch += deltaPitch;

    if (Math.abs(accumulatedYaw) >= DIRECTION_THRESHOLD_DEGREES) {
      appendStroke(player, accumulatedYaw > 0 ? GestureDirection.RIGHT : GestureDirection.LEFT);
      accumulatedYaw = 0;
    }
    if (Math.abs(accumulatedPitch) >= DIRECTION_THRESHOLD_DEGREES) {
      // Pitch increases as the player looks further DOWN, by Minecraft's convention.
      appendStroke(player, accumulatedPitch > 0 ? GestureDirection.DOWN : GestureDirection.UP);
      accumulatedPitch = 0;
    }
  }

  private void appendStroke(LocalPlayer player, GestureDirection direction) {
    // Collapse consecutive identical directions into one stroke (design
    // document section 4.2 step 3) so a slightly wobbly swipe still
    // reads as a single stroke.
    if (strokes.isEmpty() || strokes.get(strokes.size() - 1) != direction) {
      strokes.add(direction);
      GestureProgressEvents.STROKE_ADDED.invoker().onGestureStrokeAdded(player, List.copyOf(strokes));
    }
  }
}
