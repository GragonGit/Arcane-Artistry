package net.arcaneartistry.core.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Opt-in marker for "this item can be used to draw gestures" (design
 * document section 4.1/4.3). Implement this on any {@link net.minecraft.item.Item}
 * -- staffs are the obvious example, but core does not require the item to
 * be a staff, or to have anything to do with spells at all. A future
 * alchemy module's spoon/cauldron is exactly the kind of unrelated item this
 * is meant to support.
 */
public interface GestureCastable {
    /**
     * @return whether {@code player} may currently begin drawing a gesture
     * while holding {@code stack}. Typical reasons to return {@code false}:
     * the item has no usable definition loaded (e.g. its datapack content
     * was removed in a {@code /reload}), or it is on cooldown.
     */
    boolean canAttemptCast(ItemStack stack, PlayerEntity player);
}
