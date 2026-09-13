package gragongit.arcaneartistry.common.api;

import java.util.List;
import gragongit.arcaneartistry.common.staff.StaffDirection;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public final class CastProgressEvents {
  private CastProgressEvents() {}

  public interface Start {
    void onCastProgressStart(CastProgressContext castContext);
  }

  public static final Event<Start> START = EventFactory.createArrayBacked(Start.class, listeners -> (castContext) -> {
    for (Start listener : listeners) {
      listener.onCastProgressStart(castContext);
    }
  });

  public interface Hold {
    void onCastProgressHold(CastProgressContext castContext);
  }

  public static final Event<Hold> HOLD = EventFactory.createArrayBacked(Hold.class, listeners -> (castContext) -> {
    for (Hold listener : listeners) {
      listener.onCastProgressHold(castContext);
    }
  });

  public interface StrokeAdded {
    void onCastProgressStrokeAdded(CastProgressContext castContext);
  }

  public static final Event<StrokeAdded> STROKE_ADDED = EventFactory.createArrayBacked(StrokeAdded.class, listeners -> (castContext) -> {
    for (StrokeAdded listener : listeners) {
      listener.onCastProgressStrokeAdded(castContext);
    }
  });

  public interface Stop {
    void onCastProgressStop(CastProgressContext castContext);
  }

  public static final Event<Stop> STOP = EventFactory.createArrayBacked(Stop.class, listeners -> (castContext) -> {
    for (Stop listener : listeners) {
      listener.onCastProgressStop(castContext);
    }
  });

  public record CastProgressContext(Player player, List<StaffDirection> strokes) {
  }
}
