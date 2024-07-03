package de.arcane_artistry.spell.spell_type.projectile_spell;

import java.util.ArrayList;
import java.util.List;

import de.arcane_artistry.spell.spell_effect.SpellEffect;
import de.arcane_artistry.spell.spell_type.DestroyConditional;
import de.arcane_artistry.spell.spell_type.spell.SpellBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class ProjectileSpellBuilder<BUILDER extends ProjectileSpellBuilder<BUILDER, SPELL>, SPELL extends ProjectileSpell>
    extends SpellBuilder<BUILDER, SPELL> {
  protected double velocity = 2.0F;
  protected ParticleEffect particleEffect;
  private Vec3d translationOffset = Vec3d.ZERO;
  private double yaw = 0;
  private double pitch = 0;

  protected ArrayList<SpellEffect> onCollisionSpellEffects = new ArrayList<>();
  protected ArrayList<SpellEffect> onBlockHitSpellEffects = new ArrayList<>();
  protected ArrayList<SpellEffect> onBlockCollisionSpellEffects = new ArrayList<>();
  protected ArrayList<SpellEffect> onEntityHitSpellEffects = new ArrayList<>();
  protected ArrayList<SpellEffect> onPlayerCollisionSpellEffects = new ArrayList<>();

  protected DestroyConditional<HitResult> destroyOnHitResult;
  protected DestroyConditional<BlockHitResult> destroyOnBlockHitResult;
  protected DestroyConditional<BlockState> destroyOnBlockState;
  protected DestroyConditional<EntityHitResult> destroyOnEntityHitResult;
  protected DestroyConditional<PlayerEntity> destroyOnPlayerEntity;

  public BUILDER setVelocity(double velocity) {
    this.velocity = velocity;
    return self();
  }

  public BUILDER setParticleEffect(ParticleEffect particleEffect) {
    this.particleEffect = particleEffect;
    return self();
  }

  public BUILDER setTranslationOffset(double xOffset, double yOffset, double zOffset) {
    this.translationOffset = new Vec3d(xOffset, yOffset, zOffset);
    return self();
  }

  public BUILDER setYaw(double yaw) {
    this.yaw = Math.toRadians(yaw % 360);
    return self();
  }

  public BUILDER setPitch(double pitch) {
    this.pitch = Math.toRadians(pitch % 360);
    return self();
  }

  public BUILDER addOnCollisionSpellEffect(SpellEffect spellEffect) {
    onCollisionSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER setOnCollisionSpellEffect(SpellEffect spellEffect) {
    onCollisionSpellEffects.clear();
    onCollisionSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER addOnBlockHitSpellEffect(SpellEffect spellEffect) {
    onBlockHitSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER setOnBlockHitSpellEffect(SpellEffect spellEffect) {
    onBlockHitSpellEffects.clear();
    onBlockHitSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER addOnBlockCollisionSpellEffect(SpellEffect spellEffect) {
    onBlockCollisionSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER setOnBlockCollisionSpellEffect(SpellEffect spellEffect) {
    onBlockCollisionSpellEffects.clear();
    onBlockCollisionSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER addOnEntityHitSpellEffect(SpellEffect spellEffect) {
    onEntityHitSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER setOnEntityHitSpellEffect(SpellEffect spellEffect) {
    onEntityHitSpellEffects.clear();
    onEntityHitSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER addOnPlayerCollisionSpellEffect(SpellEffect spellEffect) {
    onPlayerCollisionSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER setOnPlayerCollisionSpellEffect(SpellEffect spellEffect) {
    onPlayerCollisionSpellEffects.clear();
    onPlayerCollisionSpellEffects.add(spellEffect);
    return self();
  }

  public BUILDER destroyOnCollision(DestroyConditional<HitResult> destroyOnCollision) {
    this.destroyOnHitResult = destroyOnCollision;
    return self();
  }

  public BUILDER destroyOnCollision() {
    this.destroyOnHitResult = (w, o, a) -> true;
    return self();
  }

  public BUILDER destroyOnBlockHit(DestroyConditional<BlockHitResult> destroyOnBlockHitResult) {
    this.destroyOnBlockHitResult = destroyOnBlockHitResult;
    return self();
  }

  public BUILDER destroyOnBlockHit() {
    this.destroyOnBlockHitResult = (w, o, a) -> true;
    return self();
  }

  public BUILDER destroyOnBlockCollision(DestroyConditional<BlockState> destroyOnBlockState) {
    this.destroyOnBlockState = destroyOnBlockState;
    return self();
  }

  public BUILDER destroyOnEntityHit(DestroyConditional<EntityHitResult> destroyOnEntityHitResult) {
    this.destroyOnEntityHitResult = destroyOnEntityHitResult;
    return self();
  }

  public BUILDER destroyOnEntityHit() {
    this.destroyOnEntityHitResult = (w, o, a) -> true;
    return self();
  }

  public BUILDER destroyOnPlayerCollision(DestroyConditional<PlayerEntity> destroyOnPlayerEntity) {
    this.destroyOnPlayerEntity = destroyOnPlayerEntity;
    return self();
  }

  public BUILDER destroyOnPlayerCollision() {
    this.destroyOnPlayerEntity = (w, o, a) -> true;
    return self();
  }

  public BUILDER copySpell(ProjectileSpell spell) {
    super.copySpell(spell);
    velocity = spell.getVelocity();
    particleEffect = spell.getParticleEffect();
    translationOffset = spell.getTranslationOffset();
    yaw = spell.getYaw();
    pitch = spell.getPitch();

    onCollisionSpellEffects = new ArrayList<>(spell.getOnCollisionSpellEffects());
    onBlockHitSpellEffects = new ArrayList<>(spell.getOnBlockHitSpellEffects());
    onBlockCollisionSpellEffects = new ArrayList<>(spell.getOnBlockCollisionSpellEffects());
    onEntityHitSpellEffects = new ArrayList<>(spell.getOnEntityHitSpellEffects());
    onPlayerCollisionSpellEffects = new ArrayList<>(spell.getOnPlayerCollisionSpellEffects());

    destroyOnHitResult = spell.getDestroyOnHitResult();
    destroyOnBlockHitResult = spell.getDestroyOnBlockHitResult();
    destroyOnBlockState = spell.getDestroyOnBlockState();
    destroyOnEntityHitResult = spell.getDestroyOnEntityHitResult();
    destroyOnPlayerEntity = spell.getDestroyOnPlayerEntity();
    return self();
  }

  @Override
  @SuppressWarnings("unchecked")
  public SPELL build() {
    validate();
    ProjectileSpell projectileSpell = new ProjectileSpell(pattern, velocity, particleEffect, hasNoPattern,
        translationOffset, yaw, pitch,
        List.copyOf(instantSpellEffects), List.copyOf(onCollisionSpellEffects), List.copyOf(onBlockHitSpellEffects),
        List.copyOf(onBlockCollisionSpellEffects), List.copyOf(onEntityHitSpellEffects),
        List.copyOf(onPlayerCollisionSpellEffects),
        destroyOnHitResult, destroyOnBlockHitResult, destroyOnBlockState, destroyOnEntityHitResult,
        destroyOnPlayerEntity);
    cleanup();

    return (SPELL) projectileSpell;
  }

  @Override
  protected void cleanup() {
    super.cleanup();
    this.velocity = 2.0F;
    this.particleEffect = null;
    this.translationOffset = Vec3d.ZERO;
    this.yaw = 0;
    this.pitch = 0;
    this.instantSpellEffects.clear();
    this.onCollisionSpellEffects.clear();
    this.onBlockHitSpellEffects.clear();
    this.onBlockCollisionSpellEffects.clear();
    this.onEntityHitSpellEffects.clear();
    this.onPlayerCollisionSpellEffects.clear();
    this.destroyOnHitResult = null;
    this.destroyOnBlockHitResult = null;
    this.destroyOnBlockState = null;
    this.destroyOnEntityHitResult = null;
    this.destroyOnPlayerEntity = null;
  }
}
