package net.arcaneartistry.base.registry;

import net.arcaneartistry.base.ArcaneArtistryBase;
import net.arcaneartistry.base.entity.ArcaneProjectileEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
  private static final Identifier PROJECTILE_ID = Identifier.fromNamespaceAndPath(ArcaneArtistryBase.MOD_ID,
      "arcane_projectile");

  // NOTE: passing the bare path to .build(...) below (rather than a
  // separately-built ResourceKey) matches Fabric's own current
  // first-entity example for 26.x; .maxTrackingRange/.trackingTickInterval
  // are Fabric-only builder extensions -- if these two specific calls don't
  // resolve on vanilla EntityType.Builder in your exact Fabric API version,
  // swap the builder for FabricEntityTypeBuilder, which is where they
  // normally live.
  public static final EntityType<ArcaneProjectileEntity> ARCANE_PROJECTILE = Registry.register(
      BuiltInRegistries.ENTITY_TYPE, PROJECTILE_ID,
      EntityType.Builder.<ArcaneProjectileEntity>of(ArcaneProjectileEntity::new, MobCategory.MISC)
          .sized(0.25f, 0.25f)
          .clientTrackingRange(4)
          .updateInterval(10)
          .build(ResourceKey.create(Registries.ENTITY_TYPE, PROJECTILE_ID)));

  private ModEntities() {
  }

  /**
   * Forces this class to load (and its registrations to run) deterministically
   * from {@code onInitialize}.
   */
  public static void init() {
  }
}
