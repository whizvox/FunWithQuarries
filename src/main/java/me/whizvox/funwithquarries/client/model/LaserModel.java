package me.whizvox.funwithquarries.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.whizvox.funwithquarries.FunWithQuarries;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class LaserModel extends Model {

  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(FunWithQuarries.MOD_ID, "laser"), "main");
  private final ModelPart main;

  public LaserModel(ModelPart root) {
    super(RenderType::entityCutoutNoCull);
    main = root.getChild("main");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-1.0F, -16.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)
        ),
        PartPose.offset(0.0F, 24.0F, 0.0F)
    );

    return LayerDefinition.create(meshdefinition, 16, 32);
  }

  @Override
  public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
  }

}