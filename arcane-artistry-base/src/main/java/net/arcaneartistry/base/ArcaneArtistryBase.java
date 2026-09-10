package net.arcaneartistry.base;

import net.arcaneartistry.base.registry.ModEntities;
import net.arcaneartistry.base.registry.ModItems;
import net.arcaneartistry.base.spells.SpellCooldowns;
import net.arcaneartistry.base.spells.SpellDefinition;
import net.arcaneartistry.base.spells.SpellDefinitionLoader;
import net.arcaneartistry.base.spells.SpellEffect;
import net.arcaneartistry.base.spells.SpellEffectRegistry;
import net.arcaneartistry.base.spells.effects.AreaDamageSpellEffect;
import net.arcaneartistry.base.spells.effects.BlockInteractionSpellEffect;
import net.arcaneartistry.base.spells.effects.ProjectileSpellEffect;
import net.arcaneartistry.base.spells.effects.SelfBuffSpellEffect;
import net.arcaneartistry.base.staffs.StaffDefinition;
import net.arcaneartistry.base.staffs.StaffDefinitionLoader;
import net.arcaneartistry.base.staffs.StaffItem;
import net.arcaneartistry.core.api.GestureCastCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Entry point for the base module (design document sections 5 and 6). Wires
 * item/entity registration, the two datapack reload listeners, the built-in
 * spell effect types, and the single listener that turns a core
 * {@code GestureCastCallback} into an actual cast: tag check, cooldown
 * check, effect execution.
 */
public final class ArcaneArtistryBase implements ModInitializer {
  public static final String MOD_ID = "arcane_artistry_base";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    ModItems.init();
    ModEntities.init();
    registerItemGroup();

    ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new StaffDefinitionLoader());
    ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SpellDefinitionLoader());

    registerEffects();
    GestureCastCallback.EVENT.register(this::onGestureCast);

    LOGGER.info("Arcane Artistry base module ready.");
  }

  private void registerEffects() {
    SpellEffectRegistry.register(Identifier.fromNamespaceAndPath("arcane_artistry_spells", "projectile"),
        new ProjectileSpellEffect());
    SpellEffectRegistry.register(Identifier.fromNamespaceAndPath("arcane_artistry_spells", "self_buff"),
        new SelfBuffSpellEffect());
    SpellEffectRegistry.register(Identifier.fromNamespaceAndPath("arcane_artistry_spells", "area_damage"),
        new AreaDamageSpellEffect());
    SpellEffectRegistry.register(Identifier.fromNamespaceAndPath("arcane_artistry_spells", "block_interaction"),
        new BlockInteractionSpellEffect());
  }

  private void registerItemGroup() {
    ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
        Identifier.fromNamespaceAndPath(MOD_ID, "main"));
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, FabricCreativeModeTab.builder()
        .icon(() -> new ItemStack(ModItems.FIRE_WAND))
        .title(Component.translatable("itemGroup.arcane_artistry_base"))
        .displayItems((context, output) -> {
          output.accept(ModItems.FIRE_WAND);
          output.accept(ModItems.STARTER_STAFF);
        })
        .build());
  }

  /**
   * The single place base turns a matched gesture into an actual spell
   * cast. See the README's "Implementation decisions" section for the
   * full reasoning on ordering/authority here.
   */
  private void onGestureCast(GestureCastCallback.GestureCastContext c) {
    if (matchedId.isEmpty() || !(c.stack().getItem() instanceof StaffItem staffItem)) {
      // TODO - Fizzle
      return;
    }

    SpellDefinition spell = SpellDefinitionLoader.get(matchedId.get()).orElse(null);
    if (spell == null) {
      // The matched id belongs to some other module's content sharing
      // the same gesture-pattern namespace (a future alchemy brew,
      // most likely) -- "modular by responsibility" in practice.
      return;
    }

    StaffDefinition staffDefinition = StaffDefinitionLoader.get(staffItem.getDefinitionId()).orElse(null);
    boolean allowedByStaff = staffDefinition != null && staffDefinition.allows(spell.tags());
    boolean offCooldown = SpellCooldowns.isReady(c.player(), spell.id())
        && !c.player().getCooldowns().isOnCooldown(c.stack());

    if (!allowedByStaff || !offCooldown) {
      // TODO - Fizzle
      return;
    }

    SpellEffectRegistry.get(spell.effectType()).ifPresentOrElse(
        effect -> castSpell(effect, c.player(), c.stack(), spell, staffDefinition),
        () -> LOGGER.warn("Spell {} references unknown effect type {}", spell.id(), spell.effectType()));
  }

  private void castSpell(SpellEffect effect, ServerPlayer player,
      ItemStack stack, SpellDefinition spell, StaffDefinition staffDefinition) {
    effect.execute(new SpellEffect.SpellEffectContext((ServerLevel) player.level(), player, stack, spell),
        spell.effectParams());

    SpellCooldowns.trigger(player, spell.id(), spell.cooldownTicks());
    if (staffDefinition.cooldownTicks() > 0) {
      player.getCooldowns().addCooldown(stack, staffDefinition.cooldownTicks());
    }
  }
}
