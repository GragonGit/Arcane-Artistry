package de.arcane_artistry.spell.spell_type.spell;

import java.util.ArrayList;
import java.util.List;

import de.arcane_artistry.spell.SpellMaps;
import de.arcane_artistry.spell.SpellPatternElement;
import de.arcane_artistry.spell.spell_effect.SpellEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class Spell {
  public static final SpellBuilder<?, Spell> BUILDER = new SpellBuilder<>();
  private final List<SpellPatternElement> pattern;
  private final boolean hasNoPattern;
  protected List<SpellEffect> instantSpellEffects;

  public Spell(String pattern, boolean hasNoPattern, List<SpellEffect> instantSpellEffects) {
    this.pattern = convertToSpellPattern(pattern);
    this.hasNoPattern = hasNoPattern;
    this.instantSpellEffects = instantSpellEffects;
    if (!hasNoPattern) {
      SpellMaps.registerSpell(this);
    }
  }

  public void castSpell(World world, PlayerEntity owner, Hand hand) {
    for (SpellEffect spellEffect : instantSpellEffects) {
      spellEffect.invoke(world, owner, owner.getPos());
    }
  }

  public List<SpellPatternElement> getPattern() {
    return pattern;
  }

  public boolean hasNoPattern() {
    return hasNoPattern;
  }

  public static List<SpellPatternElement> convertToSpellPattern(String pattern) {
    ArrayList<SpellPatternElement> spellPattern = new ArrayList<>();
    pattern = pattern.toUpperCase().trim();

    for (char element : pattern.toCharArray()) {
      SpellPatternElement spellElement = switch (element) {
        case 'U' -> SpellPatternElement.UP;
        case 'D' -> SpellPatternElement.DOWN;
        case 'L' -> SpellPatternElement.LEFT;
        case 'R' -> SpellPatternElement.RIGHT;
        default -> throw new IllegalArgumentException("Invalid SpellPatternElement: " + element);
      };

      spellPattern.add(spellElement);
    }

    return spellPattern;
  }
}
