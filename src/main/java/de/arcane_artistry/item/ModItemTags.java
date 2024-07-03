package de.arcane_artistry.item;

import de.arcane_artistry.ArcaneArtistry;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags {
  public static final TagKey<Item> AZURE_LOGS = newTag("azure_logs");
  public static final TagKey<Item> AZURE_LEAVES = newTag("azure_leaves");

  private static TagKey<Item> newTag(String name) {
    return TagKey.of(RegistryKeys.ITEM, ArcaneArtistry.newIdentifier(name));
  }
}
