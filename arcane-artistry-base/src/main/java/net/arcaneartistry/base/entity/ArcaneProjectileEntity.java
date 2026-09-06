package net.arcaneartistry.base.entity;

import net.arcaneartistry.base.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

/**
 * A single generic magic projectile used by every {@code projectile} spell
 * effect, distinguished only by a {@code projectileKey} (picks which vanilla
 * item it renders as) and a {@code damage} amount, both carried in its data
 * tracker so they survive tracking/(re)spawn on the client.
 *
 * <p>Extends {@code ThrownItemEntity} -- the same base vanilla uses for
 * snowballs, eggs and ender pearls -- specifically so it gets a working
 * renderer for free via {@code FlyingItemEntityRenderer} (see
 * {@code ArcaneArtistryBaseClient}, a one-line registration). A real spell
 * projectile would likely want a bespoke model eventually; this keeps v1
 * shippable without needing new art or a custom renderer, while still
 * honouring the {@code damage}/{@code speed} JSON params exactly (which a
 * bare reused vanilla {@code SmallFireballEntity} could not have, since its
 * damage isn't externally configurable).
 */
public final class ArcaneProjectileEntity extends ThrownItemEntity {
    private static final TrackedData<Float> DAMAGE =
            DataTracker.registerData(ArcaneProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<String> PROJECTILE_KEY =
            DataTracker.registerData(ArcaneProjectileEntity.class, TrackedDataHandlerRegistry.STRING);

    /** Required by {@code EntityType.Builder} for spawning from the entity type / on the client. */
    public ArcaneProjectileEntity(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
    }

    public ArcaneProjectileEntity(World world, LivingEntity owner, String projectileKey, double damage) {
        super(ModEntities.ARCANE_PROJECTILE, owner, world);
        this.getDataTracker().set(DAMAGE, (float) damage);
        this.getDataTracker().set(PROJECTILE_KEY, projectileKey);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DAMAGE, 4.0f);
        builder.add(PROJECTILE_KEY, "fireball");
    }

    @Override
    protected Item getDefaultItem() {
        return switch (this.getDataTracker().get(PROJECTILE_KEY)) {
            case "fireball" -> Items.FIRE_CHARGE;
            default -> Items.BLAZE_POWDER;
        };
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        super.onEntityHit(hitResult);
        if (this.getWorld().isClient() || hitResult.getEntity() == this.getOwner()) {
            return;
        }
        float damage = this.getDataTracker().get(DAMAGE);
        hitResult.getEntity().damage(this.getWorld().getDamageSources().thrown(this, this.getOwner()), damage);
        hitResult.getEntity().setFireTicks(60);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient()) {
            this.discard();
        }
    }
}
