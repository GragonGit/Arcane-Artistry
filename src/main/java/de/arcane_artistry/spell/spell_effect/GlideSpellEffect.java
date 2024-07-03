package de.arcane_artistry.spell.spell_effect;

import de.arcane_artistry.cast.GlideHandler;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GlideSpellEffect implements SpellEffect {
  @Override
  public void invoke(World world, Entity owner, Vec3d location) {
    if (owner instanceof ServerPlayerEntity playerEntity) {
      GlideHandler.enablePlayerGliding(playerEntity);
    }
  }
}
