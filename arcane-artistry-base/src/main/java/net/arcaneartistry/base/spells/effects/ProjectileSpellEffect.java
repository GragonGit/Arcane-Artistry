package net.arcaneartistry.base.spells.effects;

import com.google.gson.JsonObject;
import net.arcaneartistry.base.entity.ArcaneProjectileEntity;
import net.arcaneartistry.base.spells.SpellEffect;
import net.minecraft.world.phys.Vec3;

/**
 * Matches the design document's own example ({@code fireball.json}: params
 * {@code projectile}, {@code speed}, {@code damage}). "projectile" selects
 * which vanilla item {@link ArcaneProjectileEntity} renders as -- see that
 * class for why a single generic entity, rather than one Java class per
 * projectile "skin", is enough for v1.
 */
public final class ProjectileSpellEffect implements SpellEffect {
  @Override
  public void execute(SpellEffectContext context, JsonObject params) {
    String projectileKey = params.has("projectile") ? params.get("projectile").getAsString() : "fireball";
    double speed = params.has("speed") ? params.get("speed").getAsDouble() : 1.5;
    double damage = params.has("damage") ? params.get("damage").getAsDouble() : 4.0;

    var caster = context.caster();
    ArcaneProjectileEntity projectile = new ArcaneProjectileEntity(context.world(), caster, projectileKey, damage);

    Vec3 look = caster.getViewVector(1.0f);
    projectile.setPos(caster.getX(), caster.getEyeY() - 0.1, caster.getZ());
    projectile.setDeltaMovement(look.scale(speed));

    context.world().addFreshEntity(projectile);
  }
}
