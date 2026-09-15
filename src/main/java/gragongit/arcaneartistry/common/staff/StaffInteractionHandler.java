package gragongit.arcaneartistry.common.staff;

import java.util.List;
import gragongit.arcaneartistry.common.api.CastPattern;
import gragongit.arcaneartistry.common.api.CastProgressEvents;
import gragongit.arcaneartistry.common.api.CastProgressEvents.CastProgressContext;
import gragongit.arcaneartistry.common.api.CastState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public final class StaffInteractionHandler {
  private static final double INPUT_THRESHOLD = 200.0;
  private static final double MAX_STAFF_RENDER_OFFSET = 0.5;
  private static final double RENDER_TRANSLATE_SCALE = 0.00075;

  public static void register() {
    StaffInteractionEvents.START.register(StaffInteractionHandler::onStaffInteractionStart);
    StaffInteractionEvents.HOLD.register(StaffInteractionHandler::onStaffInteractionHold);
    StaffInteractionEvents.STOP.register(StaffInteractionHandler::onStaffInteractionStop);
    MouseInputCallback.EVENT.register(StaffInteractionHandler::onMouseInput);
  }

  public static void onStaffInteractionStart(Player player) {
    CastState state = CastState.of(player);
    state.setAccumulatedYaw(0);
    state.setAccumulatedPitch(0);
    state.setStaffRenderOffsetYaw(0);
    state.setStaffRenderOffsetPitch(0);
    state.clearStrokes();
    state.setCasting(true);

    player.startUsingItem(player.getUsedItemHand());
    CastProgressEvents.START.invoker().onCastProgressStart(getCastProgressContext(player));
  }

  static void onStaffInteractionHold(Player player) {
    CastProgressEvents.HOLD.invoker().onCastProgressHold(getCastProgressContext(player));
  }

  private static void appendStroke(Player player, StaffDirection direction) {
    CastState state = CastState.of(player);
    List<StaffDirection> strokes = state.getStrokes();

    if (!strokes.isEmpty() && strokes.get(strokes.size() - 1) == direction) {
      return;
    }

    state.addStroke(direction);
    CastProgressEvents.STROKE_ADDED.invoker().onCastProgressStrokeAdded(getCastProgressContext(player));
  }

  public static void onStaffInteractionStop(Player player) {
    CastState.of(player).setCasting(false);
    CastProgressEvents.STOP.invoker().onCastProgressStop(getCastProgressContext(player));
  }

  public static void cancel(Player player) {
    CastState state = CastState.of(player);
    state.setCasting(false);
    state.clearStrokes();
  }

  private static CastProgressContext getCastProgressContext(Player player) {
    return new CastProgressContext(player, new CastPattern(CastState.of(player).getStrokes()));
  }

  private static InteractionResult onMouseInput(double deltaX, double deltaY) {
    Player player = Minecraft.getInstance().player;
    if (player == null) {
      return InteractionResult.PASS;
    }

    CastState state = CastState.of(player);
    if (!state.isCasting()) {
      return InteractionResult.PASS;
    }

    double yaw = state.getAccumulatedYaw() + deltaX;
    double pitch = state.getAccumulatedPitch() + deltaY;

    if (Math.abs(yaw) >= INPUT_THRESHOLD) {
      appendStroke(player, yaw > 0 ? StaffDirection.RIGHT : StaffDirection.LEFT);
      yaw = 0;
      pitch = 0;
    }
    if (Math.abs(pitch) >= INPUT_THRESHOLD) {
      appendStroke(player, pitch > 0 ? StaffDirection.DOWN : StaffDirection.UP);
      yaw = 0;
      pitch = 0;
    }

    state.setAccumulatedYaw(yaw);
    state.setAccumulatedPitch(pitch);

    double offsetYaw =
        Mth.clamp(state.getStaffRenderOffsetYaw() + deltaX * RENDER_TRANSLATE_SCALE, -MAX_STAFF_RENDER_OFFSET, MAX_STAFF_RENDER_OFFSET);
    double offsetPitch =
        Mth.clamp(state.getStaffRenderOffsetPitch() + deltaY * RENDER_TRANSLATE_SCALE, -MAX_STAFF_RENDER_OFFSET, MAX_STAFF_RENDER_OFFSET);
    state.setStaffRenderOffsetYaw(offsetYaw);
    state.setStaffRenderOffsetPitch(offsetPitch);

    return InteractionResult.CONSUME;
  }
}
