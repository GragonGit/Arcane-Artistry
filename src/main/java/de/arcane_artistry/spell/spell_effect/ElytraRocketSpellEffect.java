package de.arcane_artistry.spell.spell_effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ElytraRocketSpellEffect implements SpellEffect {
  @Override
  public void invoke(World world, Entity owner, Vec3d location) {
    if (owner instanceof ServerPlayerEntity playerEntity && playerEntity.isFallFlying()) {
      FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, ItemStack.EMPTY, playerEntity);
      world.spawnEntity(fireworkRocketEntity);
    }
  }
}
