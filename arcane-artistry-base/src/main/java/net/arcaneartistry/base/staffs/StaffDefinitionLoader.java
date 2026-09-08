package net.arcaneartistry.base.staffs;

import com.google.gson.JsonElement;
import net.arcaneartistry.base.util.PassthroughJsonCodec;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads {@code data/<namespace>/arcane_artistry/staffs/*.json} on datapack
 * load/reload (design document sections 5 and 7).
 *
 * <p>
 * Unlike {@link net.arcaneartistry.base.spells.SpellDefinitionLoader}, staff
 * definitions don't register anything with core's gesture engine
 * themselves -- a staff is only ever a *filter* (design document section 5:
 * "this module is what turns 'any pattern is castable' into 'only patterns
 * your staff permits are castable'"), applied when a gesture cast is
 * actually attempted, not at load time.
 *
 * <p>
 * See {@link net.arcaneartistry.base.spells.SpellDefinitionLoader} for why
 * this now extends {@code SimpleJsonResourceReloadListener<JsonElement>}
 * with a pass-through codec instead of the old {@code (Gson, String)}
 * constructor -- same reasoning applies here.
 */
public final class StaffDefinitionLoader extends SimpleJsonResourceReloadListener<JsonElement>
    implements IdentifiableResourceReloadListener {
  private static final Logger LOGGER = LoggerFactory.getLogger("arcane_artistry_base/staffs");
  private static final Map<Identifier, StaffDefinition> DEFINITIONS = new ConcurrentHashMap<>();

  public StaffDefinitionLoader() {
    super(PassthroughJsonCodec.INSTANCE, FileToIdConverter.json("arcane_artistry/staffs"));
  }

  public static Optional<StaffDefinition> get(Identifier id) {
    return Optional.ofNullable(DEFINITIONS.get(id));
  }

  @Override
  protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
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
    return Identifier.fromNamespaceAndPath("arcane_artistry_base", "staffs");
  }
}
