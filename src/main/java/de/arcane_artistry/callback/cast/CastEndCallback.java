package de.arcane_artistry.callback.cast;

import java.util.List;

import de.arcane_artistry.item.staff.StaffItem;
import de.arcane_artistry.spell.SpellPatternElement;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface CastEndCallback {
  Event<CastEndCallback> EVENT =
      EventFactory.createArrayBacked(CastEndCallback.class, (listeners) -> (world, player, hand, staff, spellPattern) -> {
        for (CastEndCallback listener : listeners) {
          ActionResult result = listener.interact(world, player, hand, staff, spellPattern);

          if (result != ActionResult.PASS) {
            return result;
          }
        }

        return ActionResult.PASS;
      });

  ActionResult interact(World world, PlayerEntity player, Hand hand, StaffItem staff, List<SpellPatternElement> spellPattern);
}
