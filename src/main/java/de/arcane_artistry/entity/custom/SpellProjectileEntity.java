package de.arcane_artistry.entity.custom;

import de.arcane_artistry.spell.spell_type.projectile_spell.ProjectileSpell;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.World;

public class SpellProjectileEntity extends ProjectileEntity {
  private static final TrackedData<ParticleEffect> PARTICLE_TYPE;
  private static final TrackedData<Boolean> HAS_PARTICLE;
  private static final boolean DAMAGE = false;

  private ProjectileSpell spell;

  public SpellProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
    super(entityType, world);
  }

  public SpellProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world, ProjectileSpell spell,
      LivingEntity owner, Vec3d position, Vec3d velocity) {
    this(entityType, world);
    setOwner(owner);
    setPosition(position);
    setVelocity(velocity);
    this.spell = spell;
  }

  public void tick() {
    Entity owner = this.getOwner();
    if (!this.getWorld().isClient()
        && (owner != null && owner.isRemoved() || !this.getWorld().isChunkLoaded(this.getBlockPos()))) {
      this.discard();
      return;
    }

    super.tick();

    HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit, this.getRaycastShapeType());
    if (hitResult.getType() != Type.MISS) {
      this.onCollision(hitResult);
    }

    this.checkBlockCollision();

    Vec3d velocity = this.getVelocity();
    double nextPositionX = this.getX() + velocity.x;
    double nextPositionY = this.getY() + velocity.y;
    double nextPositionZ = this.getZ() + velocity.z;
    ProjectileUtil.setRotationFromVelocity(this, 0.2F);

    if (getParticleEffect() != null) {
      this.getWorld().addParticle(getParticleEffect(), nextPositionX, nextPositionY, nextPositionZ, 0.0, 0.0, 0.0);
    }

    this.setPosition(nextPositionX, nextPositionY, nextPositionZ);
  }

  protected void onCollision(HitResult hitResult) {
    super.onCollision(hitResult);
    if (this.spell != null) {
      spell.onCollision(this.getWorld(), this.getOwner(), this, hitResult);
    }
  }

  protected void onBlockHit(BlockHitResult blockHitResult) {
    super.onBlockHit(blockHitResult);
    if (this.spell != null) {
      spell.onBlockHit(this.getWorld(), this.getOwner(), this, blockHitResult);
    }
  }

  protected void onBlockCollision(BlockState blockState) {
    super.onBlockCollision(blockState);
    if (this.spell != null) {
      spell.onBlockCollision(this.getWorld(), this.getOwner(), this, blockState);
    }
  }

  protected void onEntityHit(EntityHitResult entityHitResult) {
    super.onEntityHit(entityHitResult);
    if (this.spell != null) {
      spell.onEntityHit(this.getWorld(), this.getOwner(), this, entityHitResult);
    }
  }

  public void onPlayerCollision(PlayerEntity playerEntity) {
    super.onPlayerCollision(playerEntity);
    if (this.spell != null) {
      spell.onPlayerCollision(this.getWorld(), this.getOwner(), this, playerEntity);
    }
  }

  public boolean shouldRender(double distance) {
    double entityAverageLength = this.getBoundingBox().getAverageSideLength() * 4.0;
    if (Double.isNaN(entityAverageLength)) {
      entityAverageLength = 4.0;
    }

    entityAverageLength *= 64.0;
    return distance < entityAverageLength * entityAverageLength;
  }

  protected RaycastContext.ShapeType getRaycastShapeType() {
    return ShapeType.COLLIDER;
  }

  protected boolean canHit(Entity entity) {
    return super.canHit(entity) && !entity.noClip;
  }

  public boolean damage(DamageSource source, float amount) {
    return DAMAGE;
  }

  public void setParticleEffect(ParticleEffect particleEffect) {
    this.dataTracker.set(PARTICLE_TYPE, particleEffect);
  }

  protected ParticleEffect getParticleEffect() {
    return this.dataTracker.get(PARTICLE_TYPE);
  }

  public void setHasParticle(boolean hasParticle) {
    this.dataTracker.set(HAS_PARTICLE, hasParticle);
  }

  protected boolean hasParticle() {
    return this.dataTracker.get(HAS_PARTICLE);
  }

  protected void initDataTracker() {
    this.dataTracker.startTracking(HAS_PARTICLE, false);
    this.dataTracker.startTracking(PARTICLE_TYPE, ParticleTypes.SMOKE);
  }

  static {
    PARTICLE_TYPE = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.PARTICLE);
    HAS_PARTICLE = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
  }
}
