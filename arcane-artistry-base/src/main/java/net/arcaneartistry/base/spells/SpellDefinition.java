package net.arcaneartistry.base.spells;

import com.google.gson.JsonObject;
import net.arcaneartistry.core.api.GestureDirection;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Parsed form of a spell definition JSON file (design document section 6.1).
 * The {@code cost} block is intentionally not modelled here at all beyond
 * being ignored -- it's reserved, inert JSON for the future mana/resource
 * system (section 9) and there is nothing for v1 to do with it yet.
 */
public record SpellDefinition(
    Identifier id,
    String name,
    List<GestureDirection> pattern,
    Set<Identifier> tags,
    int tier,
    int cooldownTicks,
    Identifier effectType,
    JsonObject effectParams) {
  public static SpellDefinition fromJson(JsonObject json) {
    Identifier id = Identifier.parse(json.get("id").getAsString());
    String name = json.has("name") ? json.get("name").getAsString() : id.getPath();

    List<GestureDirection> pattern = StreamSupport.stream(json.getAsJsonArray("pattern").spliterator(), false)
        .map(element -> GestureDirection.valueOf(element.getAsString().toUpperCase(Locale.ROOT)))
        .toList();

    Set<Identifier> tags = json.has("tags")
        ? StreamSupport.stream(json.getAsJsonArray("tags").spliterator(), false)
            .map(element -> Identifier.parse(element.getAsString()))
            .collect(Collectors.toUnmodifiableSet())
        : Set.of();

    int tier = json.has("tier") ? json.get("tier").getAsInt() : 0;
    int cooldownTicks = json.has("cooldown_ticks") ? json.get("cooldown_ticks").getAsInt() : 0;

    JsonObject effect = json.getAsJsonObject("effect");
    Identifier effectType = Identifier.parse(effect.get("type").getAsString());
    JsonObject effectParams = effect.has("params") ? effect.getAsJsonObject("params") : new JsonObject();

    return new SpellDefinition(id, name, pattern, tags, tier, cooldownTicks, effectType, effectParams);
  }
}
