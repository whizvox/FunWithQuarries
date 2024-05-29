package me.whizvox.funwithquarries.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.client.model.DroneModel;
import me.whizvox.funwithquarries.common.entity.Drone;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class DroneRenderer extends EntityRenderer<Drone> {

  private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(FunWithQuarries.MOD_ID, "textures/entity/drone.png");

  private final DroneModel model;

  public DroneRenderer(EntityRendererProvider.Context context) {
    super(context);
    model = new DroneModel(context.bakeLayer(DroneModel.LAYER_LOCATION));
  }

  @Override
  public ResourceLocation getTextureLocation(Drone entity) {
    return TEXTURE_LOCATION;
  }

  @Override
  public void render(Drone drone, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    poseStack.pushPose();
    poseStack.translate(0, -0.6, 0);
    model.setupAnim(drone, 0, 0, drone.tickCount + partialTick, 0, 0);
    VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(TEXTURE_LOCATION));
    model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    poseStack.popPose();
    super.render(drone, yaw, partialTick, poseStack, buffer, packedLight);
  }

}
