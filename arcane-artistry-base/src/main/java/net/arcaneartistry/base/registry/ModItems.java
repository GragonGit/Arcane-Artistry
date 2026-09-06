package net.arcaneartistry.base.registry;

import net.arcaneartistry.base.ArcaneArtistryBase;
import net.arcaneartistry.base.staffs.StaffItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

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
        Identifier id = Identifier.of(ArcaneArtistryBase.MOD_ID, registryPath);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

        StaffItem item = new StaffItem(
                new Item.Settings().maxCount(1).registryKey(key),
                Identifier.of(staffDefinitionId)
        );

        return Registry.register(Registries.ITEM, id, item);
    }

    /** Forces this class to load (and its registrations to run) deterministically from {@code onInitialize}. */
    public static void init() {
    }
}
