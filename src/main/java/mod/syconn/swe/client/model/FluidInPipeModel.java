package mod.syconn.swe.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.syconn.swe.Main;
import mod.syconn.swe.util.data.PipeModule;
import net.minecraft.util.FastColor;

public class FluidInPipeModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Main.loc("fluidinpipemodel"), "main");
	private final ModelPart east;
	private final ModelPart west;
	private final ModelPart north;
	private final ModelPart south;

	public FluidInPipeModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.east = root.getChild("east");
		this.west = root.getChild("west");
		this.north = root.getChild("north");
		this.south = root.getChild("south");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition east = partdefinition.addOrReplaceChild("east", CubeListBuilder.create().texOffs(0, 0).addBox(-16.3F, -10.9F, 5.2F, 11.1F, 5.8F, 5.7F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition west = partdefinition.addOrReplaceChild("west", CubeListBuilder.create().texOffs(0, 0).addBox(-10.9F, -10.9F, 5.2F, 11.1F, 5.8F, 5.7F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition north = partdefinition.addOrReplaceChild("north", CubeListBuilder.create().texOffs(-1, 0).addBox(-10.9F, -10.9F, -0.2F, 5.7F, 5.8F, 11.1F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition south = partdefinition.addOrReplaceChild("south", CubeListBuilder.create().texOffs(-1, 0).addBox(-10.9F, -10.9F, 5.2F, 5.7F, 5.8F, 11.1F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		east.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		west.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		north.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		south.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	public void renderFromModule(PipeModule module, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		if (module.isEast()) west.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		if (module.isWest()) east.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		if (module.isNorth()) north.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		if (module.isSouth()) south.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}