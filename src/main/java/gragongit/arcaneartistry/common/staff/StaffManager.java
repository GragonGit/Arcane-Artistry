package gragongit.arcaneartistry.common.staff;

import java.util.Map;
import java.util.Optional;

import net.minecraft.world.item.Item;

public final class StaffManager {
  private static Map<Item, StaffDefinition> STAFFS = Map.of();

  private StaffManager() {
  }

  public static void load(Map<Item, StaffDefinition> staffs) {
    STAFFS = Map.copyOf(staffs);
  }

  public static Optional<StaffDefinition> get(Item item) {
    return Optional.ofNullable(STAFFS.get(item));
  }
}
