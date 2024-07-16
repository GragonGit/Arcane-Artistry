package de.arcane_artistry.cast;

import java.util.HashSet;
import java.util.Set;

import de.arcane_artistry.item.staff.StaffItem;
import net.minecraft.entity.player.PlayerEntity;

public class GlideHandler {
  private static final Set<PlayerEntity> glidingPlayers = new HashSet<>();

  public static boolean isPlayerGliding(PlayerEntity player) {
    return glidingPlayers.contains(player);
  }

  public static void enablePlayerGliding(PlayerEntity player) {
    if (isPlayerGliding(player) || player.isFallFlying())
      return;

    player.startFallFlying();
    glidingPlayers.add(player);
  }

  public static void disablePlayerGliding(PlayerEntity player) {
    glidingPlayers.remove(player);
  }

  public static void checkPlayerGlidingTick(PlayerEntity player) {
    if (!isPlayerGliding(player) || player.isFallFlying() && player.getMainHandStack().getItem() instanceof StaffItem)
      return;
    disablePlayerGliding(player);
  }
}
