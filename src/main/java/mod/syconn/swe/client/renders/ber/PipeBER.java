package mod.syconn.swe.client.renders.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.syconn.swe.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import mod.syconn.swe.Main;
import mod.syconn.swe.client.model.FluidInPipeModel;
import mod.syconn.swe.client.model.FluidPipeModel;
import mod.syconn.swe.blockentities.PipeBlockEntity;
import mod.syconn.swe.blockentities.TankBlockEntity;
import mod.syconn.swe.util.Helper;
import mod.syconn.swe.util.data.FluidPointSystem;
import mod.syconn.swe.util.data.PipeModule;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class PipeBER implements BlockEntityRenderer<PipeBlockEntity> {

    private final FluidPipeModel pm;
    private final FluidInPipeModel fm;

    public PipeBER(BlockEntityRendererProvider.Context ctx) {
        pm = new FluidPipeModel(ctx.bakeLayer(FluidPipeModel.LAYER_LOCATION));
        fm = new FluidInPipeModel(ctx.bakeLayer(FluidInPipeModel.LAYER_LOCATION));
    }

    public void render(PipeBlockEntity be, float p_112308_, PoseStack ps, MultiBufferSource bs, int packedLight, int p_112312_) {
        PipeModule mod = new PipeModule(be.getBlockState());
        if (mod.isDown() || mod.isUp()) {
            ps.pushPose();
            ps.translate(1, -0.5f, 0);
            VertexConsumer vertexconsumer = bs.getBuffer(RenderType.entityCutoutNoCull(Main.loc("textures/models/ber/fluid_pipe.png")));
            this.pm.renderCase(be.getBlockState(), ps, vertexconsumer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
            ps.scale(1, 1.08f, 1);
            if (Minecraft.getInstance().level.getBlockEntity(be.getSource().pos(), Registration.TANK.get()).isPresent()) {
                TankBlockEntity te = Minecraft.getInstance().level.getBlockEntity(be.getSource().pos(), Registration.TANK.get()).get();
                if (te.getGuiTexture() != null) {
                    int i = IClientFluidTypeExtensions.of(te.getFluidTank().getFluid().getFluid()).getTintColor();
                    vertexconsumer = bs.getBuffer(RenderType.entityCutoutNoCull(te.getGuiTexture()));
                    this.pm.renderFluid(be.getBlockState(), ps, vertexconsumer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, i); // WAS Alpha 1.0F
                }
            }
            ps.popPose();
        }

        if (mod.hasFluid() && Minecraft.getInstance().level.getBlockEntity(be.getSource().pos(), Registration.TANK.get()).isPresent()) {
            TankBlockEntity te = Minecraft.getInstance().level.getBlockEntity(be.getSource().pos(), Registration.TANK.get()).get();
            if (te.getGuiTexture() != null) {
                int i = IClientFluidTypeExtensions.of(te.getFluidTank().getFluid().getFluid()).getTintColor();
                ps.pushPose();
                ps.translate(1, -0.5f, 0);
                VertexConsumer vertexconsumer = bs.getBuffer(RenderType.entityCutoutNoCull(te.getGuiTexture()));
                this.fm.renderFromModule(new PipeModule(be.getBlockState()), ps, vertexconsumer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, i);
                ps.popPose();
            }
        }

        for (FluidPointSystem.FluidPoint point : be.getSystem().getImports()) {
            double[] pos = Helper.exportPosFromDir(point.d(), false);
            float rot = 0F;
            if (point.d() == Direction.DOWN) rot = 270f;
            if (point.d() == Direction.UP) rot = 90f;
            ps.pushPose();
            if (point.d() == Direction.EAST) ps.translate(1, -0.5f, 0);
            if (point.d() == Direction.WEST) ps.translate(0, -0.5f, 1);
            if (point.d() == Direction.SOUTH) ps.translate(1, -0.5f, 1);
            if (point.d() == Direction.NORTH) ps.translate(0, -0.5f, 0);
            ps.translate(pos[0], pos[1], pos[2]);
            ps.mulPose(Axis.YP.rotationDegrees(Helper.exportFromDirection(point.d())));
            ps.mulPose(Axis.ZP.rotationDegrees(rot));
            VertexConsumer vertexconsumer = bs.getBuffer(RenderType.entityCutoutNoCull(Main.loc("textures/models/ber/fluid_pipe.png")));
            this.pm.renderImport(ps, vertexconsumer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
            ps.popPose();
        }

        for (FluidPointSystem.FluidPoint point : be.getSystem().getExports()) {
            double[] pos = Helper.exportPosFromDir(point.d(), true);
            float rot = 0F;
            if (point.d() == Direction.DOWN) rot = 270f;
            if (point.d() == Direction.UP) rot = 90f;
            ps.pushPose();
            if (point.d() == Direction.EAST) ps.translate(1.7, -0.5f, 0);
            if (point.d() == Direction.WEST) ps.translate(-0.7, -0.5f, 1);
            if (point.d() == Direction.SOUTH) ps.translate(1, -0.5f, 1.7);
            if (point.d() == Direction.NORTH) ps.translate(0, -0.5f, -.7);
            ps.translate(pos[0], pos[1], pos[2]);
            ps.mulPose(Axis.YP.rotationDegrees(Helper.exportFromDirection(point.d())));
            ps.mulPose(Axis.ZP.rotationDegrees(rot));
            VertexConsumer vertexconsumer = bs.getBuffer(RenderType.entityCutoutNoCull(Main.loc("textures/models/ber/fluid_pipe.png")));
            this.pm.renderExport(ps, vertexconsumer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
            ps.popPose();
        }
    }
}
