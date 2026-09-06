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
import net.arcaneartistry.core.FizzleDefaults;
import net.arcaneartistry.core.api.GestureCastCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new StaffDefinitionLoader());
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SpellDefinitionLoader());

        registerEffects();
        GestureCastCallback.EVENT.register(this::onGestureCast);

        LOGGER.info("Arcane Artistry base module ready.");
    }

    private void registerEffects() {
        SpellEffectRegistry.register(Identifier.of("arcane_artistry_spells", "projectile"), new ProjectileSpellEffect());
        SpellEffectRegistry.register(Identifier.of("arcane_artistry_spells", "self_buff"), new SelfBuffSpellEffect());
        SpellEffectRegistry.register(Identifier.of("arcane_artistry_spells", "area_damage"), new AreaDamageSpellEffect());
        SpellEffectRegistry.register(Identifier.of("arcane_artistry_spells", "block_interaction"), new BlockInteractionSpellEffect());
    }

    private void registerItemGroup() {
        RegistryKey<ItemGroup> key = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(MOD_ID, "main"));
        Registry.register(Registries.ITEM_GROUP, key, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModItems.FIRE_WAND))
                .displayName(Text.translatable("itemGroup.arcane_artistry_base"))
                .entries((context, entries) -> {
                    entries.add(ModItems.FIRE_WAND);
                    entries.add(ModItems.STARTER_STAFF);
                })
                .build());
    }

    /**
     * The single place base turns a matched gesture into an actual spell
     * cast. See the README's "Implementation decisions" section for the
     * full reasoning on ordering/authority here.
     */
    private void onGestureCast(net.minecraft.server.network.ServerPlayerEntity player, ItemStack stack, Hand hand,
                                Optional<Identifier> matchedId, GestureCastCallback.GestureCastContext context) {
        if (matchedId.isEmpty() || !(stack.getItem() instanceof StaffItem staffItem)) {
            // No match at all (core's FizzleDefaults already handles that
            // case), or drawn with something that isn't one of our staffs --
            // most likely a future module's own GestureCastable item. Either
            // way, not base's concern.
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
        boolean offCooldown = SpellCooldowns.isReady(player, spell.id())
                && !player.getItemCooldownManager().isCoolingDown(staffItem);

        if (!allowedByStaff || !offCooldown) {
            // The gesture matched *a* spell, just not one this staff/cooldown
            // state allows right now -- from the player's point of view this
            // should look and sound the same as any other failed cast.
            FizzleDefaults.playAt(context.world(), context.position());
            return;
        }

        SpellEffectRegistry.get(spell.effectType()).ifPresentOrElse(
                effect -> castSpell(effect, player, stack, spell, staffDefinition),
                () -> LOGGER.warn("Spell {} references unknown effect type {}", spell.id(), spell.effectType())
        );
    }

    private void castSpell(SpellEffect effect, net.minecraft.server.network.ServerPlayerEntity player,
                            ItemStack stack, SpellDefinition spell, StaffDefinition staffDefinition) {
        effect.execute(new SpellEffect.SpellEffectContext(player.getServerWorld(), player, stack, spell), spell.effectParams());

        SpellCooldowns.trigger(player, spell.id(), spell.cooldownTicks());
        if (staffDefinition.cooldownTicks() > 0) {
            player.getItemCooldownManager().set(stack.getItem(), staffDefinition.cooldownTicks());
        }
    }
}
