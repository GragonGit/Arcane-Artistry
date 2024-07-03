package de.arcane_artistry.spell.spell_type;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface DestroyConditional<T> {
  boolean shouldDestroy(World world, Entity owner, T additional);
}
