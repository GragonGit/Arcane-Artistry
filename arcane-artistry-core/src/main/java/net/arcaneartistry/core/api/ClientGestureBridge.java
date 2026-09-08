package net.arcaneartistry.core.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

/**
 * Common-code-safe indirection so a shared class -- like a staff {@code Item},
 * which is loaded on BOTH the physical client and a dedicated server -- can
 * trigger client-only gesture capture without ever referencing a client-only
 * type directly.
 *
 * <p>
 * This interface's own signature only uses common types ({@link ItemStack},
 * {@link InteractionHand}), so it is always safe to reference from shared
 * code. The real implementation, which internally deals with
 * {@code LocalPlayer} and {@code Minecraft}, is installed by
 * {@code net.arcaneartistry.core.client.ArcaneArtistryCoreClient#onInitializeClient}
 * -- a class that Fabric Loader guarantees is never loaded on a dedicated
 * server in the first place, so the unsafe types it touches are never a
 * problem there.
 *
 * <p>
 * See {@link ClientGestureBridgeHolder} for how modules read/install this,
 * and {@code net.arcaneartistry.base.staffs.StaffItem} for the pattern in use.
 */
public interface ClientGestureBridge {
  void startCast(ItemStack stack);

  void endCast(ItemStack stack, InteractionHand hand);

  /**
   * Default used on a dedicated server (where nothing should ever call
   * this in the first place, since callers should already be guarded by
   * {@code level.isClientSide()}) so a missing bridge fails safe instead of
   * throwing.
   */
  ClientGestureBridge NOOP = new ClientGestureBridge() {
    @Override
    public void startCast(ItemStack stack) {
    }

    @Override
    public void endCast(ItemStack stack, InteractionHand hand) {
    }
  };
}
