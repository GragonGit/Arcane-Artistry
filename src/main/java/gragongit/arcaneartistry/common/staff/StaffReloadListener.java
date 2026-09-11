package gragongit.arcaneartistry.common.staff;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import gragongit.arcaneartistry.common.ArcaneArtistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;

public class StaffReloadListener implements ResourceManagerReloadListener {
  private static final Codec<Item> CODEC = BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").codec();
  private static final String DIRECTORY = "staffs";

  @Override
  public void onResourceManagerReload(ResourceManager manager) {
    Map<Item, StaffDefinition> loaded = new HashMap<>();

    for (var entry : manager.listResources(DIRECTORY, id -> id.getPath().endsWith(".json")).entrySet()) {
      Identifier fileId = entry.getKey();

      try (Reader reader = new InputStreamReader(entry.getValue().open())) {
        JsonElement json = JsonParser.parseReader(reader);
        CODEC
            .parse(JsonOps.INSTANCE, json)
            .resultOrPartial(err -> ArcaneArtistry.LOGGER.warn("Skipping {}: {}", fileId, err))
            .ifPresent(item -> {
              Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
              loaded.put(item, new StaffDefinition(itemId));
              ArcaneArtistry.LOGGER.info("Registered '{}' as a magic staff (from {})", itemId, fileId);
            });
      } catch (Exception e) {
        ArcaneArtistry.LOGGER.error("Failed to parse staff definition {}", fileId, e);
      }

      StaffManager.load(loaded);
      ArcaneArtistry.LOGGER.info("Loaded {} staff definition(s)", loaded.size());
    }
  }
}
