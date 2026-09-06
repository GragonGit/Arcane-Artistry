package net.arcaneartistry.base.staffs;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads {@code data/<namespace>/arcane_artistry/staffs/*.json} on datapack
 * load/reload, exactly like a recipe or loot table (design document sections
 * 6.1 and 7 -- "This gets you /reload support and lets other mods, resource
 * packs, or data packs add/override spells for free"). Definitions are keyed
 * by their own {@code "id"} JSON field, not by file path.
 */
public final class StaffDefinitionLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("arcane_artistry_base/staffs");
    private static final Map<Identifier, StaffDefinition> DEFINITIONS = new ConcurrentHashMap<>();

    public StaffDefinitionLoader() {
        super(new Gson(), "arcane_artistry/staffs");
    }

    public static Optional<StaffDefinition> get(Identifier id) {
        return Optional.ofNullable(DEFINITIONS.get(id));
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        Map<Identifier, StaffDefinition> next = new ConcurrentHashMap<>();
        prepared.forEach((fileId, element) -> {
            try {
                StaffDefinition definition = StaffDefinition.fromJson(element.getAsJsonObject());
                next.put(definition.id(), definition);
            } catch (RuntimeException e) {
                LOGGER.error("Skipping malformed staff definition at {}: {}", fileId, e.getMessage());
            }
        });
        DEFINITIONS.clear();
        DEFINITIONS.putAll(next);
        LOGGER.info("Loaded {} staff definition(s).", DEFINITIONS.size());
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of("arcane_artistry_base", "staffs");
    }
}
