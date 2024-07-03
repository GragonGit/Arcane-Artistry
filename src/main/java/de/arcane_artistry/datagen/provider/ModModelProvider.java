package de.arcane_artistry.datagen.provider;

import de.arcane_artistry.block.ModBlocks;
import de.arcane_artistry.item.ModItems;
import de.arcane_artistry.model.ModModels;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateModelGenerator.TintType;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
  public ModModelProvider(FabricDataOutput output) {
    super(output);
  }

  @Override
  public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    // Azure Tree
    blockStateModelGenerator.registerLog(ModBlocks.AZURE_LOG).log(ModBlocks.AZURE_LOG).wood(ModBlocks.AZURE_WOOD);
    blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_AZURE_LOG).log(ModBlocks.STRIPPED_AZURE_LOG)
        .wood(ModBlocks.STRIPPED_AZURE_WOOD);
    blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.AZURE_PLANKS);
    blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.AZURE_LEAVES);
    blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SHIMMERING_AZURE_LEAVES);
    blockStateModelGenerator.registerTintableCross(ModBlocks.AZURE_SAPLING, TintType.NOT_TINTED);
    // Lapis Crystal
    registerCornerLikeOrientable(blockStateModelGenerator,
        ModBlocks.STONE_LAPIS_CRYSTAL, ModModels.LAPIS_CRYSTAL, TextureMap.all(Blocks.STONE));
    registerCornerLikeOrientable(blockStateModelGenerator,
        ModBlocks.DEEPSLATE_LAPIS_CRYSTAL, ModModels.LAPIS_CRYSTAL, TextureMap.all(Blocks.DEEPSLATE));
  }

  @Override
  public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    // Staffs
    itemModelGenerator.register(ModItems.ODDLY_SHAPED_STICK, Models.HANDHELD_ROD);
    itemModelGenerator.register(ModItems.CROOKED_STAFF, Models.HANDHELD_ROD);
    itemModelGenerator.register(ModItems.BASIC_STAFF, Models.HANDHELD_ROD);
    itemModelGenerator.register(ModItems.ADVANCED_STAFF, Models.HANDHELD_ROD);
    itemModelGenerator.register(ModItems.DEBUG_STAFF, Models.HANDHELD_ROD);

    // Lapis Crystal
    itemModelGenerator.register(ModItems.LAPIS_CRYSTAL, Models.GENERATED);

    // Misc
    itemModelGenerator.register(ModItems.SPELL_GLOSSARY, Models.GENERATED);
  }

  public static void registerCornerLikeOrientable(
      BlockStateModelGenerator blockStateModelGenerator,
      Block block,
      Model model,
      TextureMap textureMap) {
    Identifier modelID = model.upload(block, textureMap, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
        .coordinate(BlockStateVariantMap.create(Properties.NORTH, Properties.EAST, Properties.UP)
            .register(true, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelID)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, true, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelID)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(true, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelID)
                .put(VariantSettings.Y, VariantSettings.Rotation.R0))
            .register(false, false, true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelID)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelID)
                .put(VariantSettings.X, VariantSettings.Rotation.R180)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(false, true, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelID)
                .put(VariantSettings.X, VariantSettings.Rotation.R180)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270))
            .register(true, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelID)
                .put(VariantSettings.X, VariantSettings.Rotation.R180)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(false, false, false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, modelID)
                .put(VariantSettings.X, VariantSettings.Rotation.R180)
                .put(VariantSettings.Y, VariantSettings.Rotation.R0))));
  }
}