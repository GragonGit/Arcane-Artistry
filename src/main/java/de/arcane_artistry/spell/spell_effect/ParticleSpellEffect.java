package de.arcane_artistry.spell.spell_effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleSpellEffect implements SpellEffect {
  ParticleEffect particleType;
  int particleCount;
  int particleRadius;

  public ParticleSpellEffect(ParticleEffect particleType, int particleCount, int particleRadius) {
    this.particleType = particleType;
    this.particleCount = particleCount;
    this.particleRadius = particleRadius;
  }

  @Override
  public void invoke(World world, Entity owner, Vec3d location) {
    if (world instanceof ServerWorld serverWorld) {
      double heightoffset = owner instanceof LivingEntity entity ? entity.getEyeHeight(entity.getPose()) : 0;
      for (int i = 0; i < particleCount; i++) {
        Vec3d particlePosition = location.add(getParticleRadius(world), getParticleRadius(world) + heightoffset,
            getParticleRadius(world));
        serverWorld.spawnParticles(particleType, particlePosition.x, particlePosition.y, particlePosition.z, 1, 0, 0, 0,
            0);
      }
    }
  }

  @Override
  public void invokeOnHitResult(World world, Entity owner, Vec3d location, HitResult hitResult) {
    invoke(world, owner, hitResult.getPos());
  }

  private double getParticleRadius(World world) {
    return (world.random.nextDouble() - 0.5) * particleRadius;
  }
}
