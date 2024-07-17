package de.arcane_artistry.spell;

import java.util.List;
import de.arcane_artistry.item.staff.StaffItem;
import de.arcane_artistry.spell.spell_type.spell.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SpellHandler {

  public static ActionResult handleSpellCast(World world, PlayerEntity player, Hand hand, StaffItem staff,
      List<SpellPatternElement> spellPattern) {
    Spell spell = switch (staff.staffType) {
      case CROOKED_STAFF -> SpellMaps.CROOKED_STAFF_SPELLS.getSpell(spellPattern);
      case BASIC_STAFF -> SpellMaps.BASIC_STAFF_SPELLS.getSpell(spellPattern);
      case ADVANCED_STAFF -> SpellMaps.ADVANCED_STAFF_SPELLS.getSpell(spellPattern);
      case DEBUG_STAFF -> Spells.GLIDE_SPELL;
    };

    if (spell == null) {
      if (SpellMaps.spellExists(spellPattern))
        spell = Spells.FAIL_SPELL;
      else {
        return ActionResult.PASS;
      }
    }

    spell.castSpell(world, player, hand);
    return ActionResult.PASS;
  }
}
