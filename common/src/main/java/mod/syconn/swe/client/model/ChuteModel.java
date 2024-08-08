package mod.syconn.swe.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.swe.Constants;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ChuteModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Constants.loc("chute"), "main");
	private final ModelPart test;
	private final ModelPart test2;
	private final ModelPart brella;
	private final ModelPart brella2;

	public ChuteModel(ModelPart root)
	{
		this(root, RenderType::entityCutoutNoCull);
	}

	public ChuteModel(ModelPart root, Function<ResourceLocation, RenderType> renderType)
	{
		super(renderType);
		this.test = root.getChild("test");
		this.test2 = root.getChild("test2");
		this.brella = root.getChild("brella");
		this.brella2 = root.getChild("brella2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition test = partdefinition.addOrReplaceChild("test", CubeListBuilder.create().texOffs(17, 10).addBox(-6.0F, -13.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(17, 5).addBox(-7.0F, -15.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(17, 0).addBox(-8.0F, -17.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -19.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, -21.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-11.0F, -23.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-12.0F, -24.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition test2 = partdefinition.addOrReplaceChild("test2", CubeListBuilder.create().texOffs(0, 10).addBox(1.0F, 4.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(5, 7).addBox(0.0F, 2.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(5, 2).addBox(-1.0F, 0.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-2.0F, -2.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-3.0F, -4.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-4.0F, -6.0F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(5, 19).addBox(-5.0F, -7.0F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 7.0F, 0.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition brella = partdefinition.addOrReplaceChild("brella", CubeListBuilder.create().texOffs(51, 3).addBox(1.0F, 3.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(17, 39).addBox(0.0F, 0.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(34, 36).addBox(-1.0F, -3.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(51, 39).addBox(3.0F, 9.0F, -7.5F, 1.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(34, 18).addBox(2.0F, 6.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(-2.0F, -6.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, -1.0F, 0.5F, 3.1416F, 0.0F, -1.5708F));

		PartDefinition brella2 = partdefinition.addOrReplaceChild("brella2", CubeListBuilder.create().texOffs(34, 0).addBox(-1.0F, -3.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(17, 21).addBox(-2.0F, -6.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-3.0F, -9.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(51, 21).addBox(1.0F, 3.0F, -7.5F, 1.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(17, 3).addBox(0.0F, 0.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -12.0F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -3.0F, 0.5F, 0.0F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
		test.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, -1);
		test2.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, -1);
		brella.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
		brella2.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
	}
}