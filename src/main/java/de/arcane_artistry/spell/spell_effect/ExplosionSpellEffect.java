package de.arcane_artistry.spell.spell_effect;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;

public class ExplosionSpellEffect implements SpellEffect {
  private float power;
  private boolean createFire;

  public ExplosionSpellEffect(float power, boolean createFire) {
    this.power = power;
    this.createFire = createFire;
  }

  private void createExplosion(World world, Entity owner, Vec3d location) {
    world.createExplosion(owner, location.getX(), location.getY(), location.getZ(), power, createFire, ExplosionSourceType.TNT);
  }

  @Override
  public void invoke(World world, Entity owner, Vec3d location) {
    createExplosion(world, owner, location);
  }

}
