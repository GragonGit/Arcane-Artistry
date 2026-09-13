package gragongit.arcaneartistry.common.staff;

import java.util.ArrayList;
import java.util.List;
import gragongit.arcaneartistry.common.api.CastProgressEvents;
import gragongit.arcaneartistry.common.api.CastProgressEvents.CastProgressContext;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public final class StaffInteractionHandler {
  private static final double DIRECTION_THRESHOLD_DEGREES = 10.0;

  private float lockedYaw;
  private float lockedPitch;
  private double accumulatedYaw;
  private double accumulatedPitch;
  private final List<StaffDirection> strokes = new ArrayList<>();

  public void register() {
    StaffInteractionEvents.START.register(this::onStaffInteractionStart);
    StaffInteractionEvents.HOLD.register(this::onStaffInteractionHold);
    StaffInteractionEvents.STOP.register(this::onStaffInteractionStop);
  }

  public void onStaffInteractionStart(Player player) {
    lockedYaw = player.getYRot();
    lockedPitch = player.getXRot();
    accumulatedYaw = 0;
    accumulatedPitch = 0;
    strokes.clear();

    player.startUsingItem(player.getUsedItemHand());
    CastProgressEvents.START.invoker().onCastProgressStart(getCastProgressContext(player));
  }

  void onStaffInteractionHold(Player player) {
    double deltaYaw = Mth.wrapDegrees(player.getYRot() - lockedYaw);
    double deltaPitch = player.getXRot() - lockedPitch;

    player.setYRot(lockedYaw);
    player.setXRot(lockedPitch);

    accumulatedYaw += deltaYaw;
    accumulatedPitch += deltaPitch;

    if (Math.abs(accumulatedYaw) >= DIRECTION_THRESHOLD_DEGREES) {
      appendStroke(player, accumulatedYaw > 0 ? StaffDirection.RIGHT : StaffDirection.LEFT);
      accumulatedYaw = 0;
    }
    if (Math.abs(accumulatedPitch) >= DIRECTION_THRESHOLD_DEGREES) {
      appendStroke(player, accumulatedPitch > 0 ? StaffDirection.DOWN : StaffDirection.UP);
      accumulatedPitch = 0;
    }
    CastProgressEvents.HOLD.invoker().onCastProgressHold(getCastProgressContext(player));
  }

  private void appendStroke(Player player, StaffDirection direction) {
    if (!strokes.isEmpty() && strokes.get(strokes.size() - 1) == direction) {
      return;
    }

    strokes.add(direction);
    CastProgressEvents.STROKE_ADDED.invoker().onCastProgressStrokeAdded(getCastProgressContext(player));
  }

  public void onStaffInteractionStop(Player player) {
    CastProgressEvents.STOP.invoker().onCastProgressStop(getCastProgressContext(player));
  }

  public void cancel() {
    strokes.clear();
  }

  private CastProgressContext getCastProgressContext(Player player) {
    return new CastProgressContext(player, List.copyOf(strokes));
  }
}
