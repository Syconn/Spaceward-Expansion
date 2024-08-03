package mod.syconn.swe2.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.syconn.swe2.Main;
import net.minecraft.util.FastColor;

import java.util.function.Function;

public class ParachuteModel extends Model {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Main.loc("backpack"), "main");
	private final ModelPart top;
	private final ModelPart bottom;

	public ParachuteModel(ModelPart root)
	{
		this(root, RenderType::entityCutoutNoCull);
	}

	public ParachuteModel(ModelPart root, Function<ResourceLocation, RenderType> renderType)
	{
		super(renderType);
		this.top = root.getChild("top");
		this.bottom = root.getChild("bottom");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.0F, -1.0F, 10.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 6).addBox(-5.0F, -9.0F, -1.0F, 10.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		top.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		bottom.render(poseStack, vertexConsumer, packedLight, packedOverlay, FastColor.ARGB32.color(FastColor.ARGB32.alpha(color), -1));
	}
}