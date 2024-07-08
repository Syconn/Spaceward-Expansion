package mod.syconn.swe.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.swe.Main;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class FluidModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Main.loc("fluidinside"), "main");
	private final ModelPart bb_main;

	public FluidModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(1, 1).addBox(-15.6F, -15.0F, 0.2F, 15.5F, 14.0F, 15.6F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	public void renderFluid(Fluid state, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay){
		int i = IClientFluidTypeExtensions.of(state).getTintColor();
		renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, FastColor.ARGB32.color(1, i));
	}
}