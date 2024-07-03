package de.arcane_artistry.datagen.provider.lang;

import de.arcane_artistry.block.ModBlocks;
import de.arcane_artistry.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModEnglishLangProvider extends FabricLanguageProvider {

  public ModEnglishLangProvider(FabricDataOutput dataOutput) {
    super(dataOutput);
  }

  @Override
  public void generateTranslations(TranslationBuilder translationBuilder) {
    translationBuilder.add("itemgroup.arcane_artistry_group", "Arcane Artistry");

    // Staffs
    translationBuilder.add(ModItems.ODDLY_SHAPED_STICK, "Oddly Shaped Stick");
    translationBuilder.add(ModItems.CROOKED_STAFF, "Crooked Staff");
    translationBuilder.add(ModItems.BASIC_STAFF, "Basic Staff");
    translationBuilder.add(ModItems.ADVANCED_STAFF, "Advanced Staff");
    translationBuilder.add(ModItems.DEBUG_STAFF, "Debug Staff");

    // Azure Tree
    translationBuilder.add(ModBlocks.AZURE_LOG, "Azure Log");
    translationBuilder.add(ModBlocks.AZURE_WOOD, "Azure Wood");
    translationBuilder.add(ModBlocks.STRIPPED_AZURE_LOG, "Stripped Azure Log");
    translationBuilder.add(ModBlocks.STRIPPED_AZURE_WOOD, "Stripped Azure Wood");
    translationBuilder.add(ModBlocks.AZURE_PLANKS, "Azure Planks");
    translationBuilder.add(ModBlocks.AZURE_LEAVES, "Azure Leaves");
    translationBuilder.add(ModBlocks.SHIMMERING_AZURE_LEAVES, "Shimmering Azure Leaves");
    translationBuilder.add(ModBlocks.AZURE_SAPLING, "Azure Sapling");

    // Lapis Crystal
    translationBuilder.add(ModItems.LAPIS_CRYSTAL, "Lapis Crystal");
    translationBuilder.add(ModBlocks.STONE_LAPIS_CRYSTAL, "Stone Lapis Crystal");
    translationBuilder.add(ModBlocks.DEEPSLATE_LAPIS_CRYSTAL, "Deepslate Lapis Crystal");

    // Misc
    translationBuilder.add(ModItems.SPELL_GLOSSARY, "Spell Glossary");
  }
}
