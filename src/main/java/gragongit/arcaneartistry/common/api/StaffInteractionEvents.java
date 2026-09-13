package gragongit.arcaneartistry.common.api;

import java.util.List;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public final class StaffInteractionEvents {
  private StaffInteractionEvents() {}

  public interface Start {
    void onStaffInteractionStart(Player player);
  }

  public static final Event<Start> START = EventFactory.createArrayBacked(Start.class, listeners -> (player) -> {
    for (Start listener : listeners) {
      listener.onStaffInteractionStart(player);
    }
  });

  public interface Hold {
    void onStaffInteractionHold(Player player, List<StaffDirection> strokesSoFar);
  }

  public static final Event<Hold> HOLD = EventFactory.createArrayBacked(Hold.class, listeners -> (player, strokesSoFar) -> {
    for (Hold listener : listeners) {
      listener.onStaffInteractionHold(player, strokesSoFar);
    }
  });

  public interface Stop {
    void onStaffInteractionStop(Player player, List<StaffDirection> finalStrokes);
  }

  public static final Event<Stop> STOP = EventFactory.createArrayBacked(Stop.class, listeners -> (player, finalStroke) -> {
    for (Stop listener : listeners) {
      listener.onStaffInteractionStop(player, finalStroke);
    }
  });
}
