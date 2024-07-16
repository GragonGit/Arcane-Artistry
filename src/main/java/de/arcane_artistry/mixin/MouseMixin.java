package de.arcane_artistry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.sugar.Local;
import de.arcane_artistry.callback.mouse.client.MouseInputCallback;
import net.minecraft.client.Mouse;
import net.minecraft.util.ActionResult;

@Mixin(Mouse.class)
public class MouseMixin {
  @Inject(method = "updateMouse",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"),
      cancellable = true)
  private void onMouseInput(CallbackInfo info, @Local(ordinal = 2) double deltaX, @Local(ordinal = 3) double deltaY,
      @Local int invertY) {
    ActionResult result = MouseInputCallback.EVENT.invoker().interact(deltaX, deltaY * invertY);
    if (result == ActionResult.FAIL) {
      info.cancel();
    }
  }
}
