package me.whizvox.funwithquarries.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.client.model.DroneModel;
import me.whizvox.funwithquarries.client.model.LaserModel;
import me.whizvox.funwithquarries.common.entity.Drone;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DroneRenderer extends EntityRenderer<Drone> {

  private static final ResourceLocation
      DRONE_TEXTURE_LOCATION = new ResourceLocation(FunWithQuarries.MOD_ID, "textures/entity/drone.png"),
      LASER_TEXTURE_LOCATION = new ResourceLocation(FunWithQuarries.MOD_ID, "textures/entity/laser.png");

  private final DroneModel droneModel;
  private final LaserModel laserModel;

  public DroneRenderer(EntityRendererProvider.Context context) {
    super(context);
    droneModel = new DroneModel(context.bakeLayer(DroneModel.LAYER_LOCATION));
    laserModel = new LaserModel(context.bakeLayer(LaserModel.LAYER_LOCATION));
  }

  @Override
  public ResourceLocation getTextureLocation(Drone entity) {
    return DRONE_TEXTURE_LOCATION;
  }

  @Override
  public void render(Drone drone, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    poseStack.pushPose();
    poseStack.translate(0, -0.6, 0);
    droneModel.setupAnim(drone, 0, 0, drone.tickCount + partialTick, 0, 0);
    VertexConsumer vertexConsumer = buffer.getBuffer(droneModel.renderType(DRONE_TEXTURE_LOCATION));
    droneModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    poseStack.popPose();
    if (drone.getTargetType() == Drone.TargetType.BREAK || drone.getTargetType() == Drone.TargetType.PLACE) {
      // draw a bunch of 1 block long laser segments pointing from the drone's position towards the target position
      Vec3 targetPos = drone.getTargetPosition().getCenter();
      Vec3 currentPos = drone.blockPosition().getCenter();
      Vec3 direction = targetPos.subtract(currentPos).normalize();
      vertexConsumer = buffer.getBuffer(laserModel.renderType(LASER_TEXTURE_LOCATION));
      poseStack.pushPose();
      // easiest way to do this is to rotate the world in the direction of the target position
      poseStack.rotateAround(new Quaternionf().rotateTo(new Vector3f(0, 1, 0), direction.toVector3f()), 0, 0.4f, 0);
      do {
        // the origin point of the laser model is at its base
        laserModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        // since the world is rotated towards the target position, simply moving "up" will progress towards it
        poseStack.translate(0, 1, 0);
        // update the actual position for distance checking
        currentPos = currentPos.add(direction);
        // stop short of the segment that would otherwise make the laser too long
      } while (currentPos.distanceToSqr(targetPos) > 1);
      // backtrack a bit to render the final segment such that the end of the segment lies in the center of the target
      poseStack.translate(0, -1 * (1 - currentPos.distanceTo(targetPos)), 0);
      laserModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
      poseStack.popPose();
    }
    super.render(drone, yaw, partialTick, poseStack, buffer, packedLight);
  }

  @Override
  public boolean shouldRender(Drone drone, Frustum camera, double camX, double camY, double camZ) {
    Drone.TargetType type = drone.getTargetType();
    // force the drone to be drawn if lasers are drawing from it
    return super.shouldRender(drone, camera, camX, camY, camZ) || type == Drone.TargetType.BREAK || type == Drone.TargetType.PLACE;
  }

}
