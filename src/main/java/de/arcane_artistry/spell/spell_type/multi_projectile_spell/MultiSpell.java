package de.arcane_artistry.spell.spell_type.multi_projectile_spell;

import java.util.List;

import de.arcane_artistry.spell.spell_effect.SpellEffect;
import de.arcane_artistry.spell.spell_type.spell.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class MultiSpell extends Spell {
  public static final MultiSpellBuilder<?, MultiSpell> BUILDER = new MultiSpellBuilder<>();

  private List<Spell> spells;

  public MultiSpell(String pattern, boolean isSpellComponent, List<SpellEffect> instantSpellEffects, List<Spell> spells) {
    super(pattern, isSpellComponent, instantSpellEffects);
    this.spells = spells;
  }

  @Override
  public void castSpell(World world, PlayerEntity owner, Hand hand) {
    spells.forEach(spell -> spell.castSpell(world, owner, hand));
  }

  public List<Spell> getSpells() {
    return spells;
  }
}
