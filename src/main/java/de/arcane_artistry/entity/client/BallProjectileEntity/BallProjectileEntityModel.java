package de.arcane_artistry.entity.client.BallProjectileEntity;

import de.arcane_artistry.entity.custom.BallProjectileEntity;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class BallProjectileEntityModel extends SinglePartEntityModel<BallProjectileEntity> {
  private static final String MAIN = "main";
  private final ModelPart root;
  private final ModelPart projectile;

  public BallProjectileEntityModel(ModelPart root) {
    this.root = root;
    this.projectile = root.getChild(MAIN);
  }

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();
    modelPartData.addChild(MAIN, ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F),
        ModelTransform.NONE);
    return TexturedModelData.of(modelData, 64, 64);
  }

  @Override
  public void setAngles(BallProjectileEntity entity, float limbAngle, float limbDistance, float animationProgress,
      float headYaw, float headPitch) {
    this.projectile.yaw = headYaw * ((float) Math.PI / 180);
    this.projectile.pitch = headPitch * ((float) Math.PI / 180);
  }

  @Override
  public ModelPart getPart() {
    return this.root;
  }
}
