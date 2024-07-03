package de.arcane_artistry.item;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.item.staff.DebugStaffItem;
import de.arcane_artistry.item.staff.StaffItem;
import de.arcane_artistry.item.staff.StaffType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
  public static final Item ODDLY_SHAPED_STICK = registerItem("oddly_shaped_stick",
      new Item(new FabricItemSettings()));

  public static final Item CROOKED_STAFF = registerItem("crooked_staff",
      new StaffItem(new FabricItemSettings().maxCount(1), StaffType.CROOKED_STAFF, false));

  public static final Item BASIC_STAFF = registerItem("basic_staff",
      new StaffItem(new FabricItemSettings().maxCount(1), StaffType.BASIC_STAFF, true));

  public static final Item ADVANCED_STAFF = registerItem("advanced_staff",
      new StaffItem(new FabricItemSettings().maxCount(1), StaffType.ADVANCED_STAFF, true));

  public static final Item DEBUG_STAFF = registerItem("debug_staff",
      new DebugStaffItem(new FabricItemSettings().maxCount(1), StaffType.DEBUG_STAFF));

  public static final Item LAPIS_CRYSTAL = registerItem("lapis_crystal",
      new Item(new FabricItemSettings()));

  public static final Item SPELL_GLOSSARY = registerItem("spell_glossary",
      new Item(new FabricItemSettings()));

  private static Item registerItem(String name, Item item) {
    return Registry.register(Registries.ITEM, new Identifier(ArcaneArtistry.MOD_ID, name), item);
  }

  public static void registerModItems() {
    ArcaneArtistry.LOGGER.info("Registering Mod Items for " + ArcaneArtistry.MOD_ID);
  }
}
