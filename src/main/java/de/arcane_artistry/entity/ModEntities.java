package de.arcane_artistry.entity;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.entity.custom.SpellProjectileEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
  public static final EntityType<SpellProjectileEntity> SPELL_PROJECTILE_ENTITY_TYPE = registerEntityType(
      "spell_projectile", SpawnGroup.MISC, SpellProjectileEntity::new, 0.25f, 0.25f);

  public static void registerModEntities() {
    ArcaneArtistry.LOGGER.info("Registering Mod Entities for " + ArcaneArtistry.MOD_ID);
  }

  private static <T extends Entity> EntityType<T> registerEntityType(String name, SpawnGroup spawnGroup,
      EntityFactory<T> entityFactory, float width, float height) {
    return Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(ArcaneArtistry.MOD_ID, name),
        FabricEntityTypeBuilder.create(spawnGroup, entityFactory).dimensions(EntityDimensions.fixed(width, height))
            .build());
  }
}
