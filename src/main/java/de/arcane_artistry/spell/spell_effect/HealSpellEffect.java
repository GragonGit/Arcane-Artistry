package de.arcane_artistry.spell.spell_effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HealSpellEffect implements SpellEffect {
  private final float healAmount;

  public HealSpellEffect(float healAmount) {
    this.healAmount = healAmount;
  }

  private void applyHeal(LivingEntity target) {
    target.heal(healAmount);
  }

  @Override
  public void invoke(World world, Entity owner, Vec3d location) {
    if (owner instanceof LivingEntity livingEntity) {
      applyHeal(livingEntity);
    }
  }

}
