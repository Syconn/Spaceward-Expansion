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
import net.minecraft.world.level.block.state.BlockState;
import mod.syconn.swe.Main;
import mod.syconn.swe.util.data.PipeModule;

public class FluidPipeModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Main.MODID, "fluidpipemodel"), "main");
	private final ModelPart top;
	private final ModelPart bot;
	private final ModelPart top_fluid;
	private final ModelPart bot_fluid;
	private final ModelPart exporter;
	private final ModelPart importer;

	public FluidPipeModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.top = root.getChild("top");
		this.bot = root.getChild("bot");
		this.top_fluid = root.getChild("top_fluid");
		this.bot_fluid = root.getChild("bot_fluid");
		this.exporter = root.getChild("Exporter");
		this.importer = root.getChild("Importer");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 20).addBox(-11.0F, -16.0F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bot = partdefinition.addOrReplaceChild("bot", CubeListBuilder.create().texOffs(0, 20).addBox(-11.0F, -5.0F, 5.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition top_fluid = partdefinition.addOrReplaceChild("top_fluid", CubeListBuilder.create().texOffs(0, 0).addBox(-10.8F, -15.9F, 5.2F, 5.5F, 8.8F, 5.6F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bot_fluid = partdefinition.addOrReplaceChild("bot_fluid", CubeListBuilder.create().texOffs(1, 0).addBox(-10.8F, -8.9F, 5.2F, 5.5F, 8.8F, 5.6F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Exporter = partdefinition.addOrReplaceChild("Exporter", CubeListBuilder.create().texOffs(38, 39).addBox(-12.0F, -11.0F, 5.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(33, 34).addBox(-16.0F, -13.0F, 3.0F, 2.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(35, 36).addBox(-14.0F, -12.0F, 4.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Importer = partdefinition.addOrReplaceChild("Importer", CubeListBuilder.create().texOffs(35, 33).addBox(-2.75F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(35, 33).addBox(0.25F, -5.0F, -5.0F, 2.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(35, 33).addBox(-0.75F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.25F, 16.0F, 8.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {	}

	public void renderCase(BlockState state, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		PipeModule mod = new PipeModule(state);
		if (mod.isUp()) bot.render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
		if (mod.isDown()) top.render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void renderFluid(BlockState state, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		PipeModule mod = new PipeModule(state);
		if (mod.isUp() && mod.hasFluid()) bot_fluid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		if (mod.isDown() && mod.hasFluid()) top_fluid.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void renderImport(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay){
		importer.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}

	public void renderExport(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay){
		exporter.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}
}