package net.arcaneartistry.base.entity;

import net.arcaneartistry.base.registry.ModEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * A single generic magic projectile used by every {@code projectile} spell
 * effect, distinguished only by a {@code projectileKey} (picks which vanilla
 * item it renders as) and a {@code damage} amount, both carried in its
 * synced entity data so they survive tracking/(re)spawn on the client.
 *
 * <p>
 * Extends {@code ThrowableItemProjectile} -- the same base vanilla uses for
 * snowballs, eggs and ender pearls -- specifically so it gets a working
 * renderer for free via {@code ThrownItemRenderer} (see
 * {@code ArcaneArtistryBaseClient}, a one-line registration). A real spell
 * projectile would likely want a bespoke model eventually; this keeps v1
 * shippable without needing new art or a custom renderer, while still
 * honouring the {@code damage}/{@code speed} JSON params exactly (which a
 * bare reused vanilla {@code SmallFireball} could not have, since its
 * damage isn't externally configurable).
 */
public final class ArcaneProjectileEntity extends ThrowableItemProjectile {
  private static final EntityDataAccessor<Float> DAMAGE =
      SynchedEntityData.defineId(ArcaneProjectileEntity.class, EntityDataSerializers.FLOAT);

  public ArcaneProjectileEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
    super(type, level);
  }

  public ArcaneProjectileEntity(Level level, LivingEntity owner, String projectileKey, double damage) {
    super(ModEntities.ARCANE_PROJECTILE, level);
    this.setOwner(owner);
    this.getEntityData().set(DAMAGE, (float) damage);
    this.setItem(new ItemStack(resolveItem(projectileKey)));
  }

  private static Item resolveItem(String projectileKey) {
    return switch (projectileKey) {
      case "fireball" -> Items.FIRE_CHARGE;
      default -> Items.BLAZE_POWDER;
    };
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder); // safe now: getDefaultItem() no longer touches entityData
    builder.define(DAMAGE, 4.0f);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.FIRE_CHARGE; // just a construction-time placeholder, overwritten by setItem() above
  }

  @Override
  protected void onHitEntity(EntityHitResult hitResult) {
    super.onHitEntity(hitResult);
    if (!(this.level() instanceof ServerLevel serverLevel) || hitResult.getEntity() == this.getOwner()) {
      return;
    }
    float damage = this.getEntityData().get(DAMAGE);
    hitResult.getEntity().hurtServer(serverLevel, serverLevel.damageSources().thrown(this, this.getOwner()), damage);
    hitResult.getEntity().setRemainingFireTicks(60);
  }

  @Override
  protected void onHit(HitResult hitResult) {
    super.onHit(hitResult);
    if (!this.level().isClientSide()) {
      this.discard();
    }
  }
}
