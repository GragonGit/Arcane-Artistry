package de.arcane_artistry.datagen.provider;

import de.arcane_artistry.block.ModBlocks;
import de.arcane_artistry.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
  private static final float[] ODDLY_SHAPED_STICK_DROP_CHANCE = new float[] {0.05F, 0.1F, 0.25F, 0.4F};

  public ModLootTableProvider(FabricDataOutput dataOutput) {
    super(dataOutput);
  }

  @Override
  public void generate() {
    // Azure Tree
    addDrop(ModBlocks.AZURE_LOG);
    addDrop(ModBlocks.AZURE_WOOD);
    addDrop(ModBlocks.STRIPPED_AZURE_LOG);
    addDrop(ModBlocks.STRIPPED_AZURE_WOOD);
    addDrop(ModBlocks.AZURE_PLANKS);
    addDrop(ModBlocks.AZURE_LEAVES, leavesDrops(ModBlocks.AZURE_LEAVES, ModBlocks.AZURE_SAPLING, SAPLING_DROP_CHANCE));
    addDrop(ModBlocks.SHIMMERING_AZURE_LEAVES,
        leavesDrops(ModBlocks.SHIMMERING_AZURE_LEAVES, ModItems.ODDLY_SHAPED_STICK, ODDLY_SHAPED_STICK_DROP_CHANCE));
    addDrop(ModBlocks.AZURE_SAPLING);

    // Lapis Crystal
    addDrop(ModBlocks.STONE_LAPIS_CRYSTAL,
        drops(ModBlocks.STONE_LAPIS_CRYSTAL, ModItems.LAPIS_CRYSTAL, ConstantLootNumberProvider.create(1.0F)));
    addDrop(ModBlocks.DEEPSLATE_LAPIS_CRYSTAL,
        drops(ModBlocks.DEEPSLATE_LAPIS_CRYSTAL, ModItems.LAPIS_CRYSTAL, ConstantLootNumberProvider.create(1.0F)));
  }

  public LootTable.Builder leavesDrops(Block leaves, Item drop, float... chance) {
    return BlockLootTableGenerator
        .dropsWithSilkTouchOrShears(leaves,
            addSurvivesExplosionCondition(leaves, ItemEntry.builder(drop))
                .conditionally(TableBonusLootCondition.builder(Enchantments.FORTUNE, chance)))
        .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).conditionally(WITHOUT_SILK_TOUCH_NOR_SHEARS)
            .with(applyExplosionDecay(leaves,
                ItemEntry.builder(Items.STICK).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                    .conditionally(TableBonusLootCondition.builder(Enchantments.FORTUNE, LEAVES_STICK_DROP_CHANCE))));
  }
}
