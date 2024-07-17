package de.arcane_artistry.spell.spell_effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class StatusEffectSpellEffect implements SpellEffect {
  private StatusEffect statusEffectType;
  private int duration;
  private int amplifier = 1;
  private boolean ambient = false;
  private boolean showParticles = true;
  private boolean showIcon = true;

  public StatusEffectSpellEffect(StatusEffect statusEffectType, int tickDuration) {
    this.statusEffectType = statusEffectType;
    this.duration = tickDuration;
  }

  public StatusEffectSpellEffect(StatusEffect statusEffectType, int tickDuration, int amplifier) {
    this(statusEffectType, tickDuration);
    this.amplifier = amplifier;
  }

  public StatusEffectSpellEffect(StatusEffect statusEffectType, int tickDuration, int amplifier, boolean ambient,
      boolean showParticles, boolean showIcon) {
    this(statusEffectType, tickDuration, amplifier);
    this.ambient = ambient;
    this.showParticles = showParticles;
    this.showIcon = showIcon;
  }

  private void applyPotionEffect(Entity owner, LivingEntity target) {
    target.addStatusEffect(new StatusEffectInstance(statusEffectType, duration, amplifier, ambient, showParticles, showIcon),
        owner);
  }

  @Override
  public void invoke(World world, Entity owner, Vec3d location) {}

  @Override
  public void invokeOnEntityHitResult(World world, Entity owner, Vec3d location, EntityHitResult entityHitResult) {
    if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
      applyPotionEffect(owner, livingEntity);
    }
  }

}
