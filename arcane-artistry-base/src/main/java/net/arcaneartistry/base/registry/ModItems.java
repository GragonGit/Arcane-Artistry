package net.arcaneartistry.base.registry;

import net.arcaneartistry.base.ArcaneArtistryBase;
import net.arcaneartistry.base.staffs.StaffItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItems {
  // Registry path (this Item's own id) vs. staff-definition id (the JSON
  // content's own "id" field) deliberately differ, matching the
  // arcane_artistry_staffs:* ids used in the design document's own example
  // JSON and in this project's data/ files -- see StaffDefinition's javadoc.
  public static final Item FIRE_WAND = register("fire_wand", "arcane_artistry_staffs:fire_wand");
  public static final Item STARTER_STAFF = register("starter_staff", "arcane_artistry_staffs:starter_staff");

  private ModItems() {
  }

  private static Item register(String registryPath, String staffDefinitionId) {
    Identifier id = Identifier.fromNamespaceAndPath(ArcaneArtistryBase.MOD_ID, registryPath);
    ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);

    StaffItem item = new StaffItem(
        new Item.Properties().stacksTo(1).setId(key),
        Identifier.parse(staffDefinitionId));

    return Registry.register(BuiltInRegistries.ITEM, id, item);
  }

  /**
   * Forces this class to load (and its registrations to run) deterministically
   * from {@code onInitialize}.
   */
  public static void init() {
  }
}
