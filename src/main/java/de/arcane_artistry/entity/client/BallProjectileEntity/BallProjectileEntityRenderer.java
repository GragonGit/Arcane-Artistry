package de.arcane_artistry.entity.client.BallProjectileEntity;

import de.arcane_artistry.ArcaneArtistry;
import de.arcane_artistry.entity.client.ModModelLayers;
import de.arcane_artistry.entity.custom.BallProjectileEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BallProjectileEntityRenderer extends EntityRenderer<BallProjectileEntity> {
  private static final Identifier TEXTURE = new Identifier(ArcaneArtistry.MOD_ID,
      "textures/entity/ball_projectile.png");
  private final BallProjectileEntityModel model;

  public BallProjectileEntityRenderer(EntityRendererFactory.Context context) {
    super(context);
    this.model = new BallProjectileEntityModel(context.getPart(ModModelLayers.BALL_PROJECTILE_LAYER));
  }

  @Override
  public Identifier getTexture(BallProjectileEntity entity) {
    return TEXTURE;
  }

  @Override
  public void render(BallProjectileEntity ballProjectileEntity, float yaw, float tickDelta, MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider, int light) {
    matrixStack.push();
    float rotationAngleLerp = MathHelper.lerpAngleDegrees(tickDelta, ballProjectileEntity.prevYaw,
        ballProjectileEntity.getYaw());
    float pitchLerp = MathHelper.lerp(tickDelta, ballProjectileEntity.prevPitch, ballProjectileEntity.getPitch());

    this.model.setAngles(ballProjectileEntity, 0.0f, 0.0f, 0.0f, rotationAngleLerp, pitchLerp);

    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
    this.model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);

    matrixStack.pop();

    super.render(ballProjectileEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
  }
}
