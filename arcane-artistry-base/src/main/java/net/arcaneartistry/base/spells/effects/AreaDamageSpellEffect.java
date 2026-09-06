package net.arcaneartistry.base.spells.effects;

import com.google.gson.JsonObject;
import net.arcaneartistry.base.spells.SpellEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;

/** Params: {@code radius}, {@code damage}. Centred on the caster; the caster itself is excluded. */
public final class AreaDamageSpellEffect implements SpellEffect {
    @Override
    public void execute(SpellEffectContext context, JsonObject params) {
        double radius = params.has("radius") ? params.get("radius").getAsDouble() : 3.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 4.0f;

        var caster = context.caster();
        Box area = Box.of(caster.getPos(), radius * 2, radius * 2, radius * 2);

        context.world()
                .getEntitiesByClass(LivingEntity.class, area, entity -> entity != caster && entity.isAlive())
                .forEach(target -> target.damage(context.world().getDamageSources().magic(), damage));
    }
}
