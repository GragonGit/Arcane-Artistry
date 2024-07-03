package de.arcane_artistry.spell.spell_type.spell;

import java.util.ArrayList;
import java.util.List;

import de.arcane_artistry.spell.spell_effect.SpellEffect;

public class SpellBuilder<BUILDER extends SpellBuilder<BUILDER, SPELL>, SPELL extends Spell> {
  protected String pattern = "";
  protected boolean hasNoPattern = false;
  protected ArrayList<SpellEffect> instantSpellEffects = new ArrayList<>();

  public BUILDER setPattern(String pattern) {
    this.pattern = pattern;
    return self();
  }

  public BUILDER hasNoPattern() {
    this.hasNoPattern = true;
    return self();
  }

  public BUILDER addSpellEffect(SpellEffect spellEffect) {
    instantSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER setSpellEffect(SpellEffect spellEffect) {
    this.instantSpellEffects.clear();
    this.instantSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER copySpell(Spell spell) {
    this.hasNoPattern = spell.hasNoPattern();
    this.instantSpellEffects = new ArrayList<>();
    return self();
  }

  @SuppressWarnings("unchecked")
  public SPELL build() {
    validate();
    Spell spell = new Spell(pattern, hasNoPattern, List.copyOf(instantSpellEffects));
    cleanup();
    return (SPELL) spell;
  }

  @SuppressWarnings("unchecked")
  protected BUILDER self() {
    return (BUILDER) this;
  }

  protected void validate() {
    if (this.pattern.isEmpty() && !hasNoPattern) {
      throw new IllegalArgumentException("Pattern is missing");
    }
  }

  protected void cleanup() {
    this.pattern = "";
    this.hasNoPattern = false;
    this.instantSpellEffects.clear();
  }
}
