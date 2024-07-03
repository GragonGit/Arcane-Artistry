package de.arcane_artistry.block;

import de.arcane_artistry.ArcaneArtistry;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModBlockTags {
  public static final TagKey<Block> AZURE_LOGS = newTag("azure_logs");
  public static final TagKey<Block> AZURE_LEAVES = newTag("azure_leaves");

  private static TagKey<Block> newTag(String name) {
    return TagKey.of(RegistryKeys.BLOCK, ArcaneArtistry.newIdentifier(name));
  }
}
