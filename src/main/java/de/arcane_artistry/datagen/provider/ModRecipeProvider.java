package de.arcane_artistry.datagen.provider;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.block.ModBlocks;
import de.arcane_artistry.item.ModItemTags;
import de.arcane_artistry.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class ModRecipeProvider extends FabricRecipeProvider {
  public ModRecipeProvider(FabricDataOutput output) {
    super(output);
  }

  @Override
  public void generate(RecipeExporter exporter) {
    // Staffs
    ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CROOKED_STAFF).pattern("  S").pattern(" S ").pattern("S  ")
        .input('S', ModItems.ODDLY_SHAPED_STICK)
        .criterion(hasItem(ModItems.ODDLY_SHAPED_STICK), conditionsFromItem(ModItems.ODDLY_SHAPED_STICK))
        .offerTo(exporter, ArcaneArtistry.newIdentifier(getRecipeName(ModItems.CROOKED_STAFF)));
    ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BASIC_STAFF).pattern(" LD").pattern("LCL").pattern("AL ")
        .input('C', ModItems.CROOKED_STAFF).input('L', ConventionalItemTags.LAPIS).input('D', ConventionalItemTags.DIAMONDS)
        .input('A', ModItemTags.AZURE_LOGS).criterion(hasItem(ModItems.CROOKED_STAFF), conditionsFromItem(ModItems.CROOKED_STAFF))
        .offerTo(exporter, ArcaneArtistry.newIdentifier(getRecipeName(ModItems.BASIC_STAFF)));
    ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ADVANCED_STAFF).pattern("LCC").pattern("OSC").pattern("BOL")
        .input('S', ModItems.BASIC_STAFF).input('C', ModItems.LAPIS_CRYSTAL).input('L', Items.LAPIS_BLOCK)
        .input('O', Items.OBSIDIAN).input('B', Items.BLAZE_ROD)
        .criterion(hasItem(ModItems.BASIC_STAFF), conditionsFromItem(ModItems.BASIC_STAFF))
        .offerTo(exporter, ArcaneArtistry.newIdentifier(getRecipeName(ModItems.ADVANCED_STAFF)));

    // Azure Tree
    offerPlanksRecipe(exporter, ModBlocks.AZURE_PLANKS, ModItemTags.AZURE_LOGS, 4);
    offerBarkBlockRecipe(exporter, ModBlocks.AZURE_WOOD, ModBlocks.AZURE_LOG);
    offerBarkBlockRecipe(exporter, ModBlocks.STRIPPED_AZURE_WOOD, ModBlocks.STRIPPED_AZURE_LOG);
  }
}
