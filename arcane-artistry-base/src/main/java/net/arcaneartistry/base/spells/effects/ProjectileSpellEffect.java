package net.arcaneartistry.base.spells.effects;

import com.google.gson.JsonObject;
import net.arcaneartistry.base.entity.ArcaneProjectileEntity;
import net.arcaneartistry.base.spells.SpellEffect;
import net.minecraft.util.math.Vec3d;

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

        Vec3d look = caster.getRotationVec(1.0f);
        projectile.setPosition(caster.getX(), caster.getEyeY() - 0.1, caster.getZ());
        projectile.setVelocity(look.multiply(speed));

        context.world().spawnEntity(projectile);
    }
}
