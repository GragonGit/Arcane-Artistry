package de.arcane_artistry.cast.client;

import de.arcane_artistry.item.staff.StaffItem;
import de.arcane_artistry.networking.ModNetworkingConstants;
import de.arcane_artistry.spell.SpellPatternElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.ActionResult;

@Environment(value = EnvType.CLIENT)
public class CastInputHandler {
  private static final double MIN_CAST_DELTA = 200;
  private static final double MAX_STAFF_MOVEMENT = 250;

  private static boolean shouldSendPacket;
  private static double deltaX;
  private static double deltaY;
  private static double cursorX;
  private static double cursorY;

  private static KeyBinding attackKey = MinecraftClient.getInstance().options.attackKey;
  private static StaffItem activeStaff = null;

  public static ActionResult handleMouseDelta(double deltaX, double deltaY) {
    if (isCasting()) {
      updateCursorPosition(deltaX, deltaY);
      if (attackKey.isPressed()) {
        shouldSendPacket = true;

        CastInputHandler.deltaX += deltaX;
        CastInputHandler.deltaY += deltaY;
      } else {
        if (shouldSendPacket) {
          shouldSendPacket = false;

          SpellPatternElement elementToSend = mouseDeltaToSpellPatternElement(
              CastInputHandler.deltaX,
              CastInputHandler.deltaY);
          if (elementToSend != null) {
            ClientPlayNetworking.send(
                ModNetworkingConstants.SPELL_PATTERN_ELEMENT_PACKET_ID,
                PacketByteBufs.create().writeEnumConstant(elementToSend));
          }
          resetDelta();
        }
      }
      return ActionResult.FAIL;
    } else {
      resetCursorPosition();
      shouldSendPacket = false;
      return ActionResult.PASS;
    }
  }

  public static boolean isCasting() {
    return activeStaff != null;
  }

  public static void startCasting(StaffItem staff) {
    activeStaff = staff;
    resetDelta();
  }

  public static void stopCasting() {
    activeStaff = null;
    resetDelta();
  }

  public static double getCursorX() {
    return cursorX;
  }

  public static double getCursorY() {
    return cursorY;
  }

  private static void updateCursorPosition(double deltaX, double deltaY) {
    cursorX = Math.max(Math.min(cursorX + deltaX, MAX_STAFF_MOVEMENT), -MAX_STAFF_MOVEMENT);
    cursorY = Math.max(Math.min(cursorY + deltaY, MAX_STAFF_MOVEMENT), -MAX_STAFF_MOVEMENT);
  }

  private static void resetCursorPosition() {
    cursorX = 0;
    cursorY = 0;
  }

  private static void resetDelta() {
    deltaX = 0;
    deltaY = 0;
  }

  public static SpellPatternElement mouseDeltaToSpellPatternElement(double deltaX, double deltaY) {
    double absDeltaX = Math.abs(deltaX);
    double absDeltaY = Math.abs(deltaY);

    if (absDeltaX < MIN_CAST_DELTA && absDeltaY < MIN_CAST_DELTA) {
      return null;
    }

    if (absDeltaX > absDeltaY) {
      return deltaX > 0 ? SpellPatternElement.RIGHT : SpellPatternElement.LEFT;
    } else {
      return deltaY > 0 ? SpellPatternElement.DOWN : SpellPatternElement.UP;
    }
  }

  public static void onPlayerTick(ClientPlayerEntity player) {
    if (player == null || activeStaff == null)
      return;
    if (player.getMainHandStack().getItem().equals(activeStaff))
      return;
    stopCasting();
  }
}
