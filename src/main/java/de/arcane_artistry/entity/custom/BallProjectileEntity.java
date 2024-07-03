package de.arcane_artistry.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;

public class BallProjectileEntity extends ProjectileEntity {

  public BallProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
    super(entityType, world);
  }

  @Override
  protected void initDataTracker() {
  }
}
