package net.arcaneartistry.base.registry;

import net.arcaneartistry.base.ArcaneArtistryBase;
import net.arcaneartistry.base.entity.ArcaneProjectileEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public final class ModEntities {
    private static final Identifier PROJECTILE_ID = Identifier.of(ArcaneArtistryBase.MOD_ID, "arcane_projectile");
    private static final RegistryKey<EntityType<?>> PROJECTILE_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, PROJECTILE_ID);

    public static final EntityType<ArcaneProjectileEntity> ARCANE_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE, PROJECTILE_ID,
            EntityType.Builder.<ArcaneProjectileEntity>create(ArcaneProjectileEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .registryKey(PROJECTILE_KEY)
                    .build()
    );

    private ModEntities() {
    }

    /** Forces this class to load (and its registrations to run) deterministically from {@code onInitialize}. */
    public static void init() {
    }
}
