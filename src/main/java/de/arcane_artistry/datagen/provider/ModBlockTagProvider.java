package de.arcane_artistry.datagen.provider;

import java.util.concurrent.CompletableFuture;

import de.arcane_artistry.block.ModBlockTags;
import de.arcane_artistry.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
  public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup arg) {
    // Mod Block Tags
    getOrCreateTagBuilder(ModBlockTags.AZURE_LOGS).add(ModBlocks.AZURE_LOG).add(ModBlocks.AZURE_WOOD)
        .add(ModBlocks.STRIPPED_AZURE_LOG).add(ModBlocks.STRIPPED_AZURE_WOOD);
    getOrCreateTagBuilder(ModBlockTags.AZURE_LEAVES).add(ModBlocks.AZURE_LEAVES).add(ModBlocks.SHIMMERING_AZURE_LEAVES);

    // Vanilla Block Tags
    getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.STONE_LAPIS_CRYSTAL).add(ModBlocks.DEEPSLATE_LAPIS_CRYSTAL);
    getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.STONE_LAPIS_CRYSTAL).add(ModBlocks.DEEPSLATE_LAPIS_CRYSTAL);
    getOrCreateTagBuilder(BlockTags.HOE_MINEABLE).addTag(ModBlockTags.AZURE_LEAVES);
    getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).addTag(ModBlockTags.AZURE_LOGS);
    getOrCreateTagBuilder(BlockTags.PLANKS).add(ModBlocks.AZURE_PLANKS);
    getOrCreateTagBuilder(BlockTags.LEAVES).addTag(ModBlockTags.AZURE_LEAVES);
    getOrCreateTagBuilder(BlockTags.SAPLINGS).add(ModBlocks.AZURE_SAPLING);
  }
}
