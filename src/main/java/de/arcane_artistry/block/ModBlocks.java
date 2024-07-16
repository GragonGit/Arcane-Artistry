package de.arcane_artistry.block;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.block.custom.LapisCrystalBlock;
import de.arcane_artistry.world.tree.ModSaplingGenerators;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {

  // AZURE TREE
  public static final Block AZURE_LOG =
      registerBlockWithItem("azure_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)
          .mapColor(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? MapColor.DIAMOND_BLUE : MapColor.LAPIS_BLUE)));

  public static final Block STRIPPED_AZURE_LOG =
      registerBlockWithItem("stripped_azure_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG)
          .mapColor(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? MapColor.DIAMOND_BLUE : MapColor.LAPIS_BLUE)));

  public static final Block AZURE_WOOD = registerBlockWithItem("azure_wood",
      new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).mapColor(MapColor.LAPIS_BLUE)));

  public static final Block STRIPPED_AZURE_WOOD = registerBlockWithItem("stripped_azure_wood",
      new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD).mapColor(MapColor.LAPIS_BLUE)));

  public static final Block AZURE_PLANKS = registerBlockWithItem("azure_planks",
      new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).mapColor(MapColor.DIAMOND_BLUE)));

  public static final Block AZURE_LEAVES = registerBlockWithItem("azure_leaves",
      new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).mapColor(MapColor.DIAMOND_BLUE)));

  public static final Block SHIMMERING_AZURE_LEAVES = registerBlockWithItem("shimmering_azure_leaves",
      new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).mapColor(MapColor.WHITE)));

  public static final Block AZURE_SAPLING = registerBlockWithItem("azure_sapling",
      new SaplingBlock(ModSaplingGenerators.AZURE, FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)));

  // Lapis Crystal
  public static final Block STONE_LAPIS_CRYSTAL =
      registerBlockWithItem("stone_lapis_crystal", new LapisCrystalBlock(UniformIntProvider.create(2, 5),
          FabricBlockSettings.copyOf(Blocks.LAPIS_ORE).nonOpaque().ticksRandomly()));

  public static final Block DEEPSLATE_LAPIS_CRYSTAL =
      registerBlockWithItem("deepslate_lapis_crystal", new LapisCrystalBlock(UniformIntProvider.create(2, 5),
          FabricBlockSettings.copyOf(Blocks.DEEPSLATE_LAPIS_ORE).nonOpaque().ticksRandomly()));

  private static Block registerBlockWithItem(String name, Block block) {
    registerBlockItem(name, block);
    return registerBlock(name, block);
  }

  private static Block registerBlock(String name, Block block) {
    return Registry.register(Registries.BLOCK, ArcaneArtistry.newIdentifier(name), block);
  }

  private static Item registerBlockItem(String name, Block block) {
    return Registry.register(Registries.ITEM, ArcaneArtistry.newIdentifier(name), new BlockItem(block, new FabricItemSettings()));
  }

  public static void registerModBlocks() {
    ArcaneArtistry.LOGGER.info("Registering Mod Blocks for " + ArcaneArtistry.MOD_ID);
  }
}
