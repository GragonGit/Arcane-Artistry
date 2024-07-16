package de.arcane_artistry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import de.arcane_artistry.cast.client.CastInputHandler;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
  private static final double DOWNSCALE_TRANSLATE = 0.001;

  @Inject(method = "renderItem", at = @At("HEAD"))
  private void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo callbackInfo) {

    if (CastInputHandler.isCasting() && renderMode.isFirstPerson()) {
      matrices.translate(CastInputHandler.getCursorX() * DOWNSCALE_TRANSLATE,
          -CastInputHandler.getCursorY() * DOWNSCALE_TRANSLATE, 0);
    }
  }
}
