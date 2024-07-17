package de.arcane_artistry.spell;

import de.arcane_artistry.spell.spell_effect.AnvilSpellEffect;
import de.arcane_artistry.spell.spell_effect.ChatSpellEffect;
import de.arcane_artistry.spell.spell_effect.DisarmSpellEffect;
import de.arcane_artistry.spell.spell_effect.ElytraRocketSpellEffect;
import de.arcane_artistry.spell.spell_effect.ExplosionSpellEffect;
import de.arcane_artistry.spell.spell_effect.GlideSpellEffect;
import de.arcane_artistry.spell.spell_effect.HealSpellEffect;
import de.arcane_artistry.spell.spell_effect.JumpSpellEffect;
import de.arcane_artistry.spell.spell_effect.ParticleSpellEffect;
import de.arcane_artistry.spell.spell_effect.SoundSpellEffect;
import de.arcane_artistry.spell.spell_effect.StatusEffectSpellEffect;
import de.arcane_artistry.spell.spell_type.multi_projectile_spell.MultiSpell;
import de.arcane_artistry.spell.spell_type.projectile_spell.ProjectileSpell;
import de.arcane_artistry.spell.spell_type.spell.Spell;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class Spells {
  // Cast Spells
  public static final Spell FAIL_SPELL =
      Spell.BUILDER.addSpellEffect(new ParticleSpellEffect(ParticleTypes.CLOUD, 8, 2)).hasNoPattern()
          .addSpellEffect(new SoundSpellEffect(SoundEvents.ENTITY_SNIFFER_HURT, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F)).build();
  public static final Spell CHAT_SPELL =
      Spell.BUILDER.setPattern("D").addSpellEffect(new ChatSpellEffect("Play Arcane Artistry")).build();
  public static final Spell JUMP_SELF_SPELL = Spell.BUILDER.setPattern("UU").addSpellEffect(new JumpSpellEffect(1))
      .addSpellEffect(new SoundSpellEffect(SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F)).build();
  public static final Spell HEAL_SPELL = Spell.BUILDER.setPattern("LR").addSpellEffect(new HealSpellEffect(2))
      .addSpellEffect(new SoundSpellEffect(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F))
      .build();
  public static final Spell GLIDE_SPELL = Spell.BUILDER.setPattern("UULRU").addSpellEffect(new GlideSpellEffect())
      .addSpellEffect(new SoundSpellEffect(SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F)).build();
  public static final Spell ELYTRA_ROCKET_SPELL =
      Spell.BUILDER.setPattern("DDU").addSpellEffect(new ElytraRocketSpellEffect()).build();

  // Projectile Spells
  public static final ProjectileSpell FIREBALL_SPELL = ProjectileSpell.BUILDER.setPattern("UDLR")
      .addSpellEffect(new SoundSpellEffect(SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F))
      .addOnCollisionSpellEffect(new ExplosionSpellEffect(1.5F, true)).setParticleEffect(ParticleTypes.FLAME).destroyOnCollision()
      .build();
  public static final ProjectileSpell JUMP_TARGET_SPELL = ProjectileSpell.BUILDER.setPattern("UUU")
      .addOnEntityHitSpellEffect(new JumpSpellEffect(1)).setParticleEffect(ParticleTypes.CLOUD)
      .addSpellEffect(new SoundSpellEffect(SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F))
      .destroyOnCollision().build();
  public static final ProjectileSpell POISON_SPELL = ProjectileSpell.BUILDER.setPattern("LDD")
      .addOnEntityHitSpellEffect(new StatusEffectSpellEffect(StatusEffects.POISON, 5 * 20))
      .addSpellEffect(new SoundSpellEffect(SoundEvents.ENTITY_PUFFER_FISH_STING, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F))
      .setParticleEffect(ParticleTypes.ITEM_SLIME).destroyOnCollision().build();
  public static final ProjectileSpell ANVIL_SPELL =
      ProjectileSpell.BUILDER.setPattern("DDD").addOnCollisionSpellEffect(new AnvilSpellEffect(10))
          .addSpellEffect(new SoundSpellEffect(SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F))
          .setParticleEffect(ParticleTypes.SQUID_INK).destroyOnCollision().build();
  public static final ProjectileSpell DISARM_SPELL = ProjectileSpell.BUILDER.setPattern("LRUUD")
      .addSpellEffect(new SoundSpellEffect(SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F))
      .addOnEntityHitSpellEffect(new DisarmSpellEffect()).setParticleEffect(ParticleTypes.SMOKE).destroyOnCollision().build();
  public static final ProjectileSpell DISARM_MAIN_HAND_SPELL = ProjectileSpell.BUILDER.copySpell(DISARM_SPELL).setPattern("LRUR")
      .setOnEntityHitSpellEffect(new DisarmSpellEffect(Hand.MAIN_HAND)).build();
  public static final ProjectileSpell DISARM_OFF_HAND_SPELL = ProjectileSpell.BUILDER.copySpell(DISARM_SPELL).setPattern("LRUL")
      .setOnEntityHitSpellEffect(new DisarmSpellEffect(Hand.OFF_HAND)).build();
  public static final ProjectileSpell MORGANA_SPELL = ProjectileSpell.BUILDER.setPattern("LDR")
      .addOnEntityHitSpellEffect(new StatusEffectSpellEffect(StatusEffects.SLOWNESS, 5 * 20, 10))
      .addOnEntityHitSpellEffect(new StatusEffectSpellEffect(StatusEffects.JUMP_BOOST, 5 * 20, 128)) // 128 prevents jumping
      .addOnEntityHitSpellEffect(new SoundSpellEffect(SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.PLAYERS, 3F, 0.5F, 0.7F))
      .addSpellEffect(new SoundSpellEffect(SoundEvents.ENTITY_BLAZE_HURT, SoundCategory.PLAYERS, 1F, 0.3F, 0.5F))
      .setParticleEffect(ParticleTypes.SOUL).destroyOnCollision().build();

  // Multi Spells
  private static final ProjectileSpell ANIME_SPELL_COMPONENT =
      ProjectileSpell.BUILDER.addOnCollisionSpellEffect(new ExplosionSpellEffect(1.5F, true))
          .setParticleEffect(ParticleTypes.FLAME).hasNoPattern().destroyOnCollision().build();
  private static final ProjectileSpell ANIME_SPELL_COMPONENT_1 =
      ProjectileSpell.BUILDER.copySpell(ANIME_SPELL_COMPONENT).setTranslationOffset(1, 0, 0).build();
  private static final ProjectileSpell ANIME_SPELL_COMPONENT_2 =
      ProjectileSpell.BUILDER.copySpell(ANIME_SPELL_COMPONENT).setTranslationOffset(1, 1, 0).build();
  private static final ProjectileSpell ANIME_SPELL_COMPONENT_3 =
      ProjectileSpell.BUILDER.copySpell(ANIME_SPELL_COMPONENT).setTranslationOffset(0, 1, 0).build();
  private static final ProjectileSpell ANIME_SPELL_COMPONENT_4 =
      ProjectileSpell.BUILDER.copySpell(ANIME_SPELL_COMPONENT).setTranslationOffset(-1, 0, 0).build();
  private static final ProjectileSpell ANIME_SPELL_COMPONENT_5 =
      ProjectileSpell.BUILDER.copySpell(ANIME_SPELL_COMPONENT).setTranslationOffset(-1, 1, 0).build();
  private static final ProjectileSpell ANIME_SPELL_COMPONENT_6 =
      ProjectileSpell.BUILDER.copySpell(ANIME_SPELL_COMPONENT).setTranslationOffset(0, 2, 0).build();
  public static final MultiSpell ANIME_SPELL = MultiSpell.BUILDER.setPattern("LUR")
      .addSpellEffect(new SoundSpellEffect(SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1F, 1.4F, 1.6F))
      .addSpell(ANIME_SPELL_COMPONENT).addSpell(ANIME_SPELL_COMPONENT_1).addSpell(ANIME_SPELL_COMPONENT_2)
      .addSpell(ANIME_SPELL_COMPONENT_3).addSpell(ANIME_SPELL_COMPONENT_4).addSpell(ANIME_SPELL_COMPONENT_5)
      .addSpell(ANIME_SPELL_COMPONENT_6).build();
}
