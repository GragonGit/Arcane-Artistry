package de.arcane_artistry.spell.spell_effect;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class JumpSpellEffect implements SpellEffect {
  private double jumpForce;

  public JumpSpellEffect(double jumpForce) {
    this.jumpForce = jumpForce;
  }

  private void applyJump(Entity target) {
    target.addVelocity(new Vec3d(0, jumpForce, 0));
    target.velocityModified = true;
  }

  @Override
  public void invoke(World world, Entity owner, Vec3d location) {
    applyJump(owner);
  }

  @Override
  public void invokeOnEntityHitResult(World world, Entity owner, Vec3d location, EntityHitResult entityHitResult) {
    applyJump(entityHitResult.getEntity());
  }
}
