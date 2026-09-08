package net.arcaneartistry.core.client;

import net.arcaneartistry.core.api.ClientGestureBridge;
import net.arcaneartistry.core.api.ClientGestureBridgeHolder;
import net.arcaneartistry.core.network.GestureCastC2SPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

/**
 * Client entry point. This class (and everything it touches) is only ever
 * loaded on the physical client -- Fabric Loader never invokes the "client"
 * entrypoint on a dedicated server -- which is what makes it safe for this
 * file, alone among core's classes, to freely reference client-only types
 * like {@link LocalPlayer} and {@link Minecraft}.
 */
public final class ArcaneArtistryCoreClient implements ClientModInitializer {
  public static final GestureInputHandler GESTURE_INPUT = new GestureInputHandler();

  @Override
  public void onInitializeClient() {
    ClientTickEvents.END_CLIENT_TICK.register(GESTURE_INPUT::onEndClientTick);
    // Guards against a cast getting stuck "locked" if the player
    // disconnects mid-draw and reconnects.
    ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> GESTURE_INPUT.cancel());

    ClientGestureBridgeHolder.set(new ClientGestureBridge() {
      @Override
      public void startCast(ItemStack stack) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
          GESTURE_INPUT.startCast(player, stack);
        }
      }

      @Override
      public void endCast(ItemStack stack, InteractionHand hand) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
          GESTURE_INPUT.endCast(player, stack, hand);
        }
      }
    });
  }

  public static void sendCast(GestureCastC2SPayload payload) {
    ClientPlayNetworking.send(payload);
  }
}
