package de.arcane_artistry.spell.spell_type.multi_projectile_spell;

import java.util.ArrayList;
import java.util.List;

import de.arcane_artistry.spell.spell_type.spell.Spell;
import de.arcane_artistry.spell.spell_type.spell.SpellBuilder;

public class MultiSpellBuilder<BUILDER extends MultiSpellBuilder<BUILDER, SPELL>, SPELL extends MultiSpell>
    extends SpellBuilder<BUILDER, SPELL> {
  protected ArrayList<Spell> spells = new ArrayList<>();

  public BUILDER addSpell(Spell spell) {
    this.spells.add(spell);
    return self();
  }

  public BUILDER copySpell(MultiSpell spell) {
    super.copySpell(spell);
    this.spells = new ArrayList<>(spell.getSpells());
    return self();
  }

  @Override
  @SuppressWarnings("unchecked")
  public SPELL build() {
    validate();
    MultiSpell multiSpell = new MultiSpell(pattern, hasNoPattern, List.copyOf(instantSpellEffects), List.copyOf(spells));
    cleanup();

    return (SPELL) multiSpell;
  }

  @Override
  protected void cleanup() {
    super.cleanup();
    this.spells.clear();
  }
}
