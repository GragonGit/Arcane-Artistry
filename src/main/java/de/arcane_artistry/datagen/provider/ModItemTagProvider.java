package de.arcane_artistry.datagen.provider;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import de.arcane_artistry.block.ModBlockTags;
import de.arcane_artistry.item.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
  public ModItemTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture,
      @Nullable BlockTagProvider blockTagProvider) {
    super(output, completableFuture, blockTagProvider);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup arg) {
    // Mod Item Tags
    copy(ModBlockTags.AZURE_LOGS, ModItemTags.AZURE_LOGS);
    copy(ModBlockTags.AZURE_LEAVES, ModItemTags.AZURE_LEAVES);

    // Vanilla Item Tags
    copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
    copy(BlockTags.PLANKS, ItemTags.PLANKS);
    copy(BlockTags.LEAVES, ItemTags.LEAVES);
    copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
  }
}
