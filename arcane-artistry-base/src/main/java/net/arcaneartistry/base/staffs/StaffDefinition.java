package net.arcaneartistry.base.staffs;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Parsed form of a staff definition JSON file (design document section 5).
 *
 * <p>Identity is the JSON's own {@code "id"} field (e.g.
 * {@code arcane_artistry_staffs:fire_wand}), not the file's path -- the
 * design document's own examples give staffs and spells ids in namespaces
 * distinct from the mod that ships them ({@code arcane_artistry_staffs},
 * {@code arcane_artistry_spells}) even though the files themselves live
 * under {@code data/arcane_artistry_base/...} (section 11). This mirrors how
 * {@code arcane_artistry} is used as a fixed *folder* segment in that same
 * path -- analogous to vanilla's {@code recipes}/{@code loot_tables} folder
 * names -- while also being reused as an actual Identifier *namespace* for
 * tag vocabulary like {@code arcane_artistry:cantrip}. Two independent uses
 * of the same word; see the README for the long version.
 */
public record StaffDefinition(Identifier id, Set<Identifier> allowedTags, int cooldownTicks) {

    public static StaffDefinition fromJson(JsonObject json) {
        Identifier id = Identifier.of(json.get("id").getAsString());

        Set<Identifier> allowedTags = json.has("allowed_tags")
                ? StreamSupport.stream(json.getAsJsonArray("allowed_tags").spliterator(), false)
                    .map(element -> Identifier.of(element.getAsString()))
                    .collect(Collectors.toUnmodifiableSet())
                : Set.of();

        int cooldownTicks = json.has("cooldown_ticks") ? json.get("cooldown_ticks").getAsInt() : 0;

        return new StaffDefinition(id, allowedTags, cooldownTicks);
    }

    /** "a spell can only be cast by a staff if the spell's own tag set intersects with the staff's allowed_tags" (section 5). */
    public boolean allows(Set<Identifier> spellTags) {
        return !java.util.Collections.disjoint(allowedTags, spellTags);
    }
}
