package gragongit.arcaneartistry.common.registry;

import java.util.Optional;
import gragongit.arcaneartistry.common.staff.Staff;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public final class RegistryUtils {
  private RegistryUtils() {}

  public static Optional<Staff> tryGetStaff(Level level, Item item) {
    return Optional.ofNullable(level.registryAccess().lookupOrThrow(ModRegistries.STAFF_KEY).getValue(BuiltInRegistries.ITEM.getKey(item)));
  }

  public static boolean isStaff(Level level, Item item) {
    return tryGetStaff(level, item).isPresent();
  }
}
