package net.arcaneartistry.base.spells;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * A resolved handler for one {@code effect.type} value (design document
 * section 6.1: "effect.type is a registry key, resolved to a Java SpellEffect
 * implementation... new effect types are still Java, but new spells using
 * existing effect types are pure JSON"). {@code params} is the spell's own
 * {@code effect.params} JSON object, left unparsed until here so each effect
 * type is free to define its own parameter shape.
 */
public interface SpellEffect {
    void execute(SpellEffectContext context, JsonObject params);

    record SpellEffectContext(ServerWorld world, ServerPlayerEntity caster, ItemStack staffStack, SpellDefinition spell) {
    }
}
