package net.arcaneartistry.base.spells;

import com.google.gson.JsonElement;
import net.arcaneartistry.base.util.PassthroughJsonCodec;
import net.arcaneartistry.core.api.GesturePatternRegistry;
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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads {@code data/<namespace>/arcane_artistry/spells/*.json} on datapack
 * load/reload (design document sections 6.1 and 7), and additionally owns
 * registering/unregistering those spells' patterns in core's
 * {@code GesturePatternRegistry}.
 *
 * <p>
 * Core's registry has no idea a reload even happened, let alone which
 * entries in it are "ours" -- so on every {@link #apply}, this drops exactly
 * the set of ids it registered last time before adding the freshly-loaded
 * set, rather than ever clearing the registry wholesale (which would also
 * blow away a future alchemy module's brew patterns).
 *
 * <p>
 * {@code SimpleJsonResourceReloadListener} is generic and codec-driven in
 * this Minecraft version -- there's no longer a {@code (Gson, String)}
 * constructor, and extending it as a raw type breaks {@code apply}'s
 * override (the compiler erases the supertype method to a raw
 * {@code Map}). Parameterizing as {@code <JsonElement>} and passing
 * {@link PassthroughJsonCodec#INSTANCE} keeps {@link #apply} receiving
 * exactly the {@code Map<Identifier, JsonElement>} it always did, so the
 * existing {@link SpellDefinition#fromJson} parsing didn't need to change.
 */
public final class SpellDefinitionLoader extends SimpleJsonResourceReloadListener<JsonElement>
    implements IdentifiableResourceReloadListener {
  private static final Logger LOGGER = LoggerFactory.getLogger("arcane_artistry_base/spells");
  private static final Map<Identifier, SpellDefinition> DEFINITIONS = new ConcurrentHashMap<>();
  private static volatile Set<Identifier> lastRegisteredWithCore = Set.of();

  public SpellDefinitionLoader() {
    super(PassthroughJsonCodec.INSTANCE, FileToIdConverter.json("arcane_artistry/spells"));
  }

  public static Optional<SpellDefinition> get(Identifier id) {
    return Optional.ofNullable(DEFINITIONS.get(id));
  }

  @Override
  protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
    Map<Identifier, SpellDefinition> next = new ConcurrentHashMap<>();
    prepared.forEach((fileId, element) -> {
      try {
        SpellDefinition definition = SpellDefinition.fromJson(element.getAsJsonObject());
        next.put(definition.id(), definition);
      } catch (RuntimeException e) {
        LOGGER.error("Skipping malformed spell definition at {}: {}", fileId, e.getMessage());
      }
    });

    GesturePatternRegistry.unregisterAll(lastRegisteredWithCore);
    next.values().forEach(definition -> GesturePatternRegistry.register(definition.id(), definition.pattern()));
    lastRegisteredWithCore = Set.copyOf(next.keySet());

    DEFINITIONS.clear();
    DEFINITIONS.putAll(next);
    LOGGER.info("Loaded {} spell definition(s).", DEFINITIONS.size());
  }

  @Override
  public Identifier getFabricId() {
    return Identifier.fromNamespaceAndPath("arcane_artistry_base", "spells");
  }
}
