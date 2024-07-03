package de.arcane_artistry.cast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.arcane_artistry.callback.cast.CastEndCallback;
import de.arcane_artistry.item.staff.StaffItem;
import de.arcane_artistry.spell.SpellPatternElement;
import de.arcane_artistry.spell.spell_effect.SoundSpellEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.sound.SoundCategory;

public class CastHandler {

  private static Map<ServerPlayerEntity, List<SpellPatternElement>> spellPatternBuffer = new HashMap<>();
  private static List<ServerPlayerEntity> playerHolding = new ArrayList<>();

  public static void receiveSpellPatternElement(ServerPlayerEntity player, SpellPatternElement element) {
    playCastSoundEffect(player, element);
    addSpellPatternElementToPatternBuffer(player, element);
  }

  private static void playCastSoundEffect(ServerPlayerEntity player, SpellPatternElement element) {
    float pitch = switch (element) {
      case UP -> 1.2F;
      case DOWN -> 0.9F;
      case LEFT -> 1F;
      case RIGHT -> 1.1F;
    };
    new SoundSpellEffect(SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE.value(), SoundCategory.PLAYERS, 0.2F, pitch)
        .invoke(player.getWorld(), player, player.getPos());
  }

  private static void addSpellPatternElementToPatternBuffer(ServerPlayerEntity player, SpellPatternElement element) {
    if (spellPatternBuffer.containsKey(player)) {
      spellPatternBuffer.get(player).add(element);
    }
  }

  public static void onSpellCastStart(World world, ServerPlayerEntity player, StaffItem staff) {
    spellPatternBuffer.put(player, new ArrayList<SpellPatternElement>());
  }

  public static void onSpellCastEnd(World world, ServerPlayerEntity player, StaffItem staff) {
    if (isHoldingSpell(player, staff)) {
      playerHolding.add(player);
      return;
    }
    if (spellPatternBuffer.containsKey(player)) {
      List<SpellPatternElement> spellPattern = spellPatternBuffer.get(player);
      if (spellPattern != null && spellPattern.size() > 0) {
        CastEndCallback.EVENT.invoker().interact(world, player, player.getActiveHand(), staff, spellPattern);
      }
      spellPatternBuffer.remove(player);
      playerHolding.remove(player);
    }
  }

  public static void tickHoldingSpell(ServerPlayerEntity player) {
    if (player == null || !playerHolding.contains(player))
      return;
    if (player.getMainHandStack().getItem() instanceof StaffItem staffItem) {
      if (isHoldingSpell(player, staffItem))
        return;
      onSpellCastEnd(player.getWorld(), player, staffItem);
    } else {
      playerHolding.remove(player);
    }
  }

  private static boolean isHoldingSpell(ServerPlayerEntity player, StaffItem staffItem) {
    return player.isSneaking() && staffItem.canHoldSpells();
  }
}
