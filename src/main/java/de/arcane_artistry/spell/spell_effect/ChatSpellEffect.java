package de.arcane_artistry.spell.spell_effect;

import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ChatSpellEffect implements SpellEffect {
  private String message;

  public ChatSpellEffect(String message) {
    this.message = message;
  }

  private void sendMessage(Entity target) {
    target.sendMessage(Text.literal(message));
  }

  public void invoke(World world, Entity owner, Vec3d location) {
    sendMessage(owner);
  }

}
