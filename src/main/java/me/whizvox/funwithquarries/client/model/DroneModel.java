package me.whizvox.funwithquarries.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.entity.Drone;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class DroneModel extends EntityModel<Drone> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(FunWithQuarries.MOD_ID, "drone"), "main");
	private final ModelPart main;

	public DroneModel(ModelPart root) {
		main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0)
						.addBox(-4.0F, -12.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(0, 16).addBox(-5.0F, -5.0F, 3.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 20).addBox(-5.0F, -5.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
						.texOffs(0, 16).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 16).addBox(-5.0F, -13.0F, -5.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 16).addBox(-5.0F, -13.0F, 3.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 20).addBox(3.0F, -5.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
						.texOffs(0, 20).addBox(3.0F, -13.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
						.texOffs(0, 20).addBox(-5.0F, -13.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-5.0F, -11.0F, -5.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(3.0F, -11.0F, -5.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(3.0F, -11.0F, 3.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-5.0F, -11.0F, 3.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F)
		);

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Drone entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		main.yRot = ageInTicks * 0.31415926535897932384626433832795F; // pi / 10 = 1 rotation per second
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}