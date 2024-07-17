package de.arcane_artistry.item;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroups {
  public static final ItemGroup ARCANE_ARTISTRY_GROUP =
      Registry.register(Registries.ITEM_GROUP, ArcaneArtistry.newIdentifier("arcane_artistry_group"),
          FabricItemGroup.builder().displayName(Text.translatable("itemgroup.arcane_artistry_group"))
              .icon(() -> new ItemStack(ModItems.ODDLY_SHAPED_STICK)).entries((displayContext, entries) -> {

                // Staffs
                entries.add(ModItems.ODDLY_SHAPED_STICK);
                entries.add(ModItems.CROOKED_STAFF);
                entries.add(ModItems.BASIC_STAFF);
                entries.add(ModItems.ADVANCED_STAFF);

                // Azure Tree
                entries.add(ModBlocks.AZURE_LOG);
                entries.add(ModBlocks.AZURE_WOOD);
                entries.add(ModBlocks.STRIPPED_AZURE_LOG);
                entries.add(ModBlocks.STRIPPED_AZURE_WOOD);
                entries.add(ModBlocks.AZURE_PLANKS);
                entries.add(ModBlocks.AZURE_LEAVES);
                entries.add(ModBlocks.SHIMMERING_AZURE_LEAVES);
                entries.add(ModBlocks.AZURE_SAPLING);

                // Lapis Crystal
                entries.add(ModItems.LAPIS_CRYSTAL);
                entries.add(ModBlocks.STONE_LAPIS_CRYSTAL);
                entries.add(ModBlocks.DEEPSLATE_LAPIS_CRYSTAL);
              }).build());

  public static void registerModItemGroups() {
    ArcaneArtistry.LOGGER.info("Registering Item Groups for " + ArcaneArtistry.MOD_ID);
  }
}
