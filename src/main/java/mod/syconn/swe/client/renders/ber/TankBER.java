package mod.syconn.swe.client.renders.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import mod.syconn.swe.client.model.FluidModel;
import mod.syconn.swe.common.be.TankBlockEntity;

public class TankBER implements BlockEntityRenderer<TankBlockEntity> {

    private final FluidModel model;

    public TankBER(BlockEntityRendererProvider.Context ctx) {
        model = new FluidModel(ctx.bakeLayer(FluidModel.LAYER_LOCATION));
    }

    @Override
    public void render(TankBlockEntity be, float p_112308_, PoseStack ps, MultiBufferSource bs, int packedLight, int p) {
        if (!be.getFluidTank().isEmpty()) {
            ps.pushPose();
            float i = (float) (be.getFluidTank().getFluidAmount()) / be.getFluidTank().getCapacity();
            ps.translate(1, 0, 0);
            ps.scale(1, i, 1);
            ps.translate(0, -0.5, 0);
            VertexConsumer vertexconsumer = bs.getBuffer(RenderType.entityCutoutNoCull(be.getFluidTexture()));
            model.renderFluid(be.getFluidTank().getFluid().getFluid(), ps, vertexconsumer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
            ps.popPose();
        }
    }
}
