package gragongit.arcaneartistry.common.staff;

import java.util.ArrayList;
import java.util.List;
import gragongit.arcaneartistry.common.api.CastPattern;
import gragongit.arcaneartistry.common.api.CastProgressEvents;
import gragongit.arcaneartistry.common.api.CastProgressEvents.CastProgressContext;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public final class StaffInteractionHandler {
  private static final double INPUT_THRESHOLD = 500.0;

  private static StaffInteractionHandler instance;

  private double deltaX;
  private double deltaY;
  private double accumulatedYaw;
  private double accumulatedPitch;
  private final List<StaffDirection> strokes = new ArrayList<>();
  private boolean isCasting;
  private Player player;
  private InteractionHand castingHand;

  public void register() {
    instance = this;
    StaffInteractionEvents.START.register(this::onStaffInteractionStart);
    StaffInteractionEvents.HOLD.register(this::onStaffInteractionHold);
    StaffInteractionEvents.STOP.register(this::onStaffInteractionStop);
    MouseInputCallback.EVENT.register(this::onMouseInput);
  }

  public static StaffInteractionHandler getInstance() {
    return instance;
  }

  public void onStaffInteractionStart(Player player) {
    accumulatedYaw = 0;
    accumulatedPitch = 0;
    strokes.clear();
    isCasting = true;
    this.player = player;
    castingHand = player.getUsedItemHand();

    player.startUsingItem(castingHand);
    CastProgressEvents.START.invoker().onCastProgressStart(getCastProgressContext(player));
  }

  void onStaffInteractionHold(Player player) {
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
    isCasting = false;
    CastProgressEvents.STOP.invoker().onCastProgressStop(getCastProgressContext(player));
    this.player = null;
  }

  public void cancel() {
    isCasting = false;
    this.player = null;
    strokes.clear();
  }

  private CastProgressContext getCastProgressContext(Player player) {
    return new CastProgressContext(player, new CastPattern(List.copyOf(strokes)));
  }

  private InteractionResult onMouseInput(double deltaX, double deltaY) {
    if (!isCasting) {
      return InteractionResult.PASS;
    }

    this.deltaX = deltaX;
    this.deltaY = deltaY;

    accumulatedYaw += deltaX;
    accumulatedPitch += deltaY;

    if (Math.abs(accumulatedYaw) >= INPUT_THRESHOLD) {
      appendStroke(player, accumulatedYaw > 0 ? StaffDirection.RIGHT : StaffDirection.LEFT);
      accumulatedPitch = 0;
      accumulatedYaw = 0;
    }
    if (Math.abs(accumulatedPitch) >= INPUT_THRESHOLD) {
      appendStroke(player, accumulatedPitch > 0 ? StaffDirection.DOWN : StaffDirection.UP);
      accumulatedPitch = 0;
      accumulatedYaw = 0;
    }

    return InteractionResult.CONSUME;
  }

  public double getDeltaX() {
    return deltaX;
  }

  public double getDeltaY() {
    return deltaY;
  }

  public boolean isCasting() {
    return isCasting;
  }

  public InteractionHand getCastingHand() {
    return castingHand;
  }
}
