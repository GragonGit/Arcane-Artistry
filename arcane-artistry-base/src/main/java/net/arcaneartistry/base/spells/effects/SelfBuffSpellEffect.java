package net.arcaneartistry.base.spells.effects;

import com.google.gson.JsonObject;
import net.arcaneartistry.base.ArcaneArtistryBase;
import net.arcaneartistry.base.spells.SpellEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/** Params: {@code effect} (a status effect id, e.g. "minecraft:speed"), {@code duration} (ticks), {@code amplifier}. */
public final class SelfBuffSpellEffect implements SpellEffect {
    @Override
    public void execute(SpellEffectContext context, JsonObject params) {
        Identifier effectId = Identifier.of(params.get("effect").getAsString());
        int duration = params.has("duration") ? params.get("duration").getAsInt() : 200;
        int amplifier = params.has("amplifier") ? params.get("amplifier").getAsInt() : 0;

        var entry = Registries.STATUS_EFFECT.getEntry(effectId).orElse(null);
        if (entry == null) {
            ArcaneArtistryBase.LOGGER.warn("Spell {} references unknown status effect {}", context.spell().id(), effectId);
            return;
        }

        context.caster().addStatusEffect(new StatusEffectInstance(entry, duration, amplifier));
    }
}
