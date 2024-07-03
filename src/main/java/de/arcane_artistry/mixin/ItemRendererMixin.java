package de.arcane_artistry.mixin;

import de.arcane_artistry.cast.client.CastInputHandler;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
  private final double DOWNSCALETRANSLATE = 0.001;

  @Inject(method = "renderItem", at = @At("HEAD"))
  private void renderItem(
      ItemStack stack,
      ModelTransformationMode renderMode,
      boolean leftHanded,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      int overlay,
      BakedModel model,
      CallbackInfo callbackInfo) {

    if (CastInputHandler.isCasting() && renderMode.isFirstPerson()) {
      matrices.translate(CastInputHandler.getCursorX() * DOWNSCALETRANSLATE,
          -CastInputHandler.getCursorY() * DOWNSCALETRANSLATE, 0);
    }
  }
}
