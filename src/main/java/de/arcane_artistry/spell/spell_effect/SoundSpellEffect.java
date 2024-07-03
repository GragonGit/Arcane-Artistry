package de.arcane_artistry.spell.spell_effect;

import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SoundSpellEffect implements SpellEffect {
  SoundEvent soundEvent;
  SoundCategory soundCategory;
  float volume;
  float minPitch;
  float maxPitch;

  public SoundSpellEffect(SoundEvent soundEvent, SoundCategory soundCategory, float volume, float minPitch,
      float maxPitch) {
    this.soundEvent = soundEvent;
    this.soundCategory = soundCategory;
    this.volume = volume;
    this.minPitch = minPitch;
    this.maxPitch = maxPitch;
  }

  public SoundSpellEffect(SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch) {
    this(soundEvent, soundCategory, volume, pitch, pitch);
  }

  public SoundSpellEffect(SoundEvent soundEvent, SoundCategory soundCategory) {
    this(soundEvent, soundCategory, 1F, 1F);
  }

  private void playSound(World world, Vec3d location) {
    world.playSound(null, location.getX(), location.getY(), location.getZ(), soundEvent, soundCategory, volume,
        minPitch + world.getRandom().nextFloat() * (maxPitch - minPitch));
  }

  public void invoke(World world, Entity owner, Vec3d location) {
    playSound(world, location);
  }
}
