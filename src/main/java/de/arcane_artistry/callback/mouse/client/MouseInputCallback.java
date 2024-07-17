package de.arcane_artistry.callback.mouse.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface MouseInputCallback {
  Event<MouseInputCallback> EVENT = EventFactory.createArrayBacked(MouseInputCallback.class, (listeners) -> (deltaX, deltaY) -> {
    for (MouseInputCallback listener : listeners) {
      ActionResult result = listener.interact(deltaX, deltaY);

      if (result != ActionResult.PASS) {
        return result;
      }
    }

    return ActionResult.PASS;
  });

  ActionResult interact(double deltaX, double deltaY);
}
