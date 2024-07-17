package de.arcane_artistry.spell;

import java.util.HashSet;
import java.util.List;

import de.arcane_artistry.spell.spell_type.spell.Spell;

public class SpellMaps {
  private static final HashSet<List<SpellPatternElement>> ALL_SPELLS = new HashSet<>();

  public static final SpellMap CROOKED_STAFF_SPELLS = new SpellMap(List.of(Spells.CHAT_SPELL, Spells.DISARM_OFF_HAND_SPELL,
      Spells.HEAL_SPELL, Spells.JUMP_SELF_SPELL, Spells.JUMP_TARGET_SPELL));

  public static final SpellMap BASIC_STAFF_SPELLS = new SpellMap(List.of(Spells.CHAT_SPELL, Spells.DISARM_OFF_HAND_SPELL,
      Spells.HEAL_SPELL, Spells.JUMP_SELF_SPELL, Spells.JUMP_TARGET_SPELL,
      // Basic Spells
      Spells.ANVIL_SPELL, Spells.DISARM_MAIN_HAND_SPELL, Spells.FIREBALL_SPELL, Spells.MORGANA_SPELL, Spells.POISON_SPELL));

  public static final SpellMap ADVANCED_STAFF_SPELLS = new SpellMap(List.of(Spells.CHAT_SPELL, Spells.DISARM_OFF_HAND_SPELL,
      Spells.HEAL_SPELL, Spells.JUMP_SELF_SPELL, Spells.JUMP_TARGET_SPELL,
      // Basic Spells
      Spells.ANVIL_SPELL, Spells.DISARM_MAIN_HAND_SPELL, Spells.FIREBALL_SPELL, Spells.MORGANA_SPELL, Spells.POISON_SPELL,
      // Advanced Spells
      Spells.ANIME_SPELL, Spells.DISARM_SPELL, Spells.ELYTRA_ROCKET_SPELL, Spells.GLIDE_SPELL));

  public static boolean spellExists(List<SpellPatternElement> spellPattern) {
    return ALL_SPELLS.contains(spellPattern);
  }

  public static void registerSpell(Spell spell) {
    ALL_SPELLS.add(spell.getPattern());
  }
}
