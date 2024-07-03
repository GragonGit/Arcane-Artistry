package de.arcane_artistry.spell.spell_type.projectile_spell;

import java.util.List;

import de.arcane_artistry.entity.ModEntities;
import de.arcane_artistry.entity.custom.SpellProjectileEntity;
import de.arcane_artistry.spell.spell_effect.SpellEffect;
import de.arcane_artistry.spell.spell_type.DestroyConditional;
import de.arcane_artistry.spell.spell_type.spell.Spell;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileSpell extends Spell {
  public static final ProjectileSpellBuilder<?, ProjectileSpell> BUILDER = new ProjectileSpellBuilder<>();

  private double velocity;
  private ParticleEffect particleEffect;
  private Vec3d translationOffset;
  private double yaw;
  private double pitch;

  private List<SpellEffect> onCollisionSpellEffects;
  private List<SpellEffect> onBlockHitSpellEffects;
  private List<SpellEffect> onBlockCollisionSpellEffects;
  private List<SpellEffect> onEntityHitSpellEffects;
  private List<SpellEffect> onPlayerCollisionSpellEffects;

  private DestroyConditional<HitResult> destroyOnHitResult;
  private DestroyConditional<BlockHitResult> destroyOnBlockHitResult;
  private DestroyConditional<BlockState> destroyOnBlockState;
  private DestroyConditional<EntityHitResult> destroyOnEntityHitResult;
  private DestroyConditional<PlayerEntity> destroyOnPlayerEntity;

  public ProjectileSpell(String pattern, double velocity, ParticleEffect particleEffect, boolean isSpellComponent,
      Vec3d translationOffset, double yaw, double pitch,
      List<SpellEffect> instantSpellEffects, List<SpellEffect> onCollisionSpellEffects,
      List<SpellEffect> onBlockHitSpellEffects, List<SpellEffect> onBlockCollisionSpellEffects,
      List<SpellEffect> onEntityHitSpellEffects, List<SpellEffect> onPlayerCollisionSpellEffects,
      DestroyConditional<HitResult> destroyOnHitResult, DestroyConditional<BlockHitResult> destroyOnBlockHitResult,
      DestroyConditional<BlockState> destroyOnBlockState, DestroyConditional<EntityHitResult> destroyOnEntityHitResult,
      DestroyConditional<PlayerEntity> destroyOnPlayerEntity) {
    super(pattern, isSpellComponent, instantSpellEffects);
    this.velocity = velocity;
    this.particleEffect = particleEffect;
    this.translationOffset = translationOffset;
    this.yaw = yaw;
    this.pitch = pitch;
    this.onCollisionSpellEffects = onCollisionSpellEffects;
    this.onBlockHitSpellEffects = onBlockHitSpellEffects;
    this.onBlockCollisionSpellEffects = onBlockCollisionSpellEffects;
    this.onEntityHitSpellEffects = onEntityHitSpellEffects;
    this.onPlayerCollisionSpellEffects = onPlayerCollisionSpellEffects;
    this.destroyOnHitResult = destroyOnHitResult;
    this.destroyOnBlockHitResult = destroyOnBlockHitResult;
    this.destroyOnBlockState = destroyOnBlockState;
    this.destroyOnEntityHitResult = destroyOnEntityHitResult;
    this.destroyOnPlayerEntity = destroyOnPlayerEntity;
  }

  /**
   * Is called once on any collision block or entity
   * Basically a culmination of onBlockHit and onEntityHit
   * 
   * @param hitResult
   */
  public void onCollision(World world, Entity owner, SpellProjectileEntity projectile, HitResult hitResult) {
    for (SpellEffect spellEffect : onCollisionSpellEffects) {
      spellEffect.invokeOnHitResult(world, owner, projectile.getPos(), hitResult);
    }
    if (destroyOnHitResult != null && destroyOnHitResult.shouldDestroy(world, owner, hitResult)) {
      projectile.discard();
    }
  }

  /**
   * Is called once when entering minecraft blocks on a new tick
   * This means it is called everytime when shooting through a row of blocks but
   * only once when it hits multiple blocks in the same tick e.g. a 2x2 area
   * 
   * @param blockHitResult
   */
  public void onBlockHit(World world, Entity owner, SpellProjectileEntity projectile,
      BlockHitResult blockHitResult) {
    for (SpellEffect spellEffect : onBlockHitSpellEffects) {
      spellEffect.invokeOnBlockHitResult(world, owner, projectile.getPos(), blockHitResult);
    }
    if (destroyOnBlockHitResult != null && destroyOnBlockHitResult.shouldDestroy(world, owner, blockHitResult)) {
      projectile.discard();
    }
  }

  /**
   * Is called when entering any new block
   * This means unlike onBlockHit it is called for every block it hits even if it
   * is the same tick e.g. 4 times on a 2x2 area
   * Does NOT ignore air by default
   * 
   * @param blockState
   */
  public void onBlockCollision(World world, Entity owner, SpellProjectileEntity projectile, BlockState blockState) {
    for (SpellEffect spellEffect : onBlockCollisionSpellEffects) {
      spellEffect.invokeOnBlockState(world, owner, projectile.getPos(), blockState);
    }
    if (destroyOnBlockState != null && destroyOnBlockState.shouldDestroy(world, owner, blockState)) {
      projectile.discard();
    }
  }

  /**
   * Is called once when entering any entity
   * 
   * @param entityHitResult
   */
  public void onEntityHit(World world, Entity owner, SpellProjectileEntity projectile,
      EntityHitResult entityHitResult) {
    for (SpellEffect spellEffect : onEntityHitSpellEffects) {
      spellEffect.invokeOnEntityHitResult(world, owner, projectile.getPos(), entityHitResult);
    }
    if (destroyOnEntityHitResult != null && destroyOnEntityHitResult.shouldDestroy(world, owner, entityHitResult)) {
      projectile.discard();
    }
  }

  /**
   * Is called once when entering any player
   * 
   * @param playerEntity
   */
  public void onPlayerCollision(World world, Entity owner, SpellProjectileEntity projectile,
      PlayerEntity playerEntity) {
    for (SpellEffect spellEffect : onPlayerCollisionSpellEffects) {
      spellEffect.invokeOnPlayerEntity(world, owner, projectile.getPos(), playerEntity);
    }
    if (destroyOnPlayerEntity != null && destroyOnPlayerEntity.shouldDestroy(world, owner, playerEntity)) {
      projectile.discard();
    }
  }

  @Override
  public void castSpell(World world, PlayerEntity player, Hand hand) {
    super.castSpell(world, player, hand);

    Vec3d lookForward = player.getRotationVector();
    Vec3d lookRight = lookForward.crossProduct(new Vec3d(0, 1, 0)).normalize();
    Vec3d lookUp = lookRight.crossProduct(lookForward).normalize();

    Vec3d offset = lookRight.multiply(translationOffset.x)
        .add(lookUp.multiply(translationOffset.y))
        .add(lookForward.multiply(translationOffset.z));

    Vec3d rotateYaw = rotate(lookForward, lookUp, yaw);
    Vec3d projectileVelocity = rotate(rotateYaw, lookRight.negate(), pitch).multiply(velocity);

    SpellProjectileEntity projectile = new SpellProjectileEntity(
        ModEntities.SPELL_PROJECTILE_ENTITY_TYPE,
        world,
        this,
        player,
        player.getPos().add(new Vec3d(0, 1.5, 0)).add(offset),
        projectileVelocity);

    if (particleEffect != null) {
      projectile.setHasParticle(true);
      projectile.setParticleEffect(particleEffect);
    }

    world.spawnEntity(projectile);
  }

  private Vec3d rotate(Vec3d vector, Vec3d axis, double angle) {
    if (angle == 0) {
      return vector;
    }

    Vec3d vecParallel = axis.multiply(vector.dotProduct(axis) / axis.dotProduct(axis));
    Vec3d vecOrthogonal = vector.subtract(vecParallel);
    Vec3d crossProduct = vecOrthogonal.crossProduct(axis);
    double length = vecOrthogonal.length();
    double cosOffset = Math.cos(angle) / length;
    double sinOffset = Math.sin(angle) / length;
    Vec3d vecRotated = vecOrthogonal.multiply(cosOffset).add(crossProduct.multiply(sinOffset)).multiply(length);

    return vecRotated.add(vecParallel);
  }

  public double getVelocity() {
    return velocity;
  }

  public ParticleEffect getParticleEffect() {
    return particleEffect;
  }

  public Vec3d getTranslationOffset() {
    return translationOffset;
  }

  public double getYaw() {
    return yaw;
  }

  public double getPitch() {
    return pitch;
  }

  public List<SpellEffect> getOnCollisionSpellEffects() {
    return onCollisionSpellEffects;
  }

  public List<SpellEffect> getOnBlockHitSpellEffects() {
    return onBlockHitSpellEffects;
  }

  public List<SpellEffect> getOnBlockCollisionSpellEffects() {
    return onBlockCollisionSpellEffects;
  }

  public List<SpellEffect> getOnEntityHitSpellEffects() {
    return onEntityHitSpellEffects;
  }

  public List<SpellEffect> getOnPlayerCollisionSpellEffects() {
    return onPlayerCollisionSpellEffects;
  }

  public DestroyConditional<HitResult> getDestroyOnHitResult() {
    return destroyOnHitResult;
  }

  public DestroyConditional<BlockHitResult> getDestroyOnBlockHitResult() {
    return destroyOnBlockHitResult;
  }

  public DestroyConditional<BlockState> getDestroyOnBlockState() {
    return destroyOnBlockState;
  }

  public DestroyConditional<EntityHitResult> getDestroyOnEntityHitResult() {
    return destroyOnEntityHitResult;
  }

  public DestroyConditional<PlayerEntity> getDestroyOnPlayerEntity() {
    return destroyOnPlayerEntity;
  }

}
