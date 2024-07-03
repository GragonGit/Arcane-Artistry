package de.arcane_artistry.spell;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.arcane_artistry.spell.spell_type.spell.Spell;

public class SpellMap extends HashMap<List<SpellPatternElement>, Spell> {

  public SpellMap(Collection<Spell> spells) {
    addSpells(spells);
  }

  public void addSpells(Collection<Spell> spells) {
    for (Spell spell : spells) {
      this.putSpell(spell);
    }
  }

  public void putSpell(Spell spell) throws IllegalArgumentException {
    if (this.containsKey(spell.getPattern())) {
      throw new IllegalArgumentException("Pattern already used");
    }
    if (spell.hasNoPattern()) {
      throw new IllegalArgumentException("Spell is only a Spell Component");
    }
    super.put(spell.getPattern(), spell);
  }

  public Spell getSpell(List<SpellPatternElement> spellPattern) {
    return super.get(spellPattern);
  }
}
