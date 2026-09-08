package net.arcaneartistry.base.spells;

import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

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

  record SpellEffectContext(ServerLevel world, ServerPlayer caster, ItemStack staffStack, SpellDefinition spell) {
  }
}
