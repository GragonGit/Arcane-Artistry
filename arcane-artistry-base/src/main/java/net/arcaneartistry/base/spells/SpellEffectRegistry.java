package net.arcaneartistry.base.spells;

import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code effect.type} Identifier -> Java {@link SpellEffect} implementation.
 */
public final class SpellEffectRegistry {
  private static final Map<Identifier, SpellEffect> EFFECTS = new ConcurrentHashMap<>();

  private SpellEffectRegistry() {
  }

  public static void register(Identifier type, SpellEffect effect) {
    EFFECTS.put(type, effect);
  }

  public static Optional<SpellEffect> get(Identifier type) {
    return Optional.ofNullable(EFFECTS.get(type));
  }
}
