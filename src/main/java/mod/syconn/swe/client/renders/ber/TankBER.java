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
import mod.syconn.swe.blockentities.TankBlockEntity;

public class TankBER implements BlockEntityRenderer<TankBlockEntity> {

    private final FluidModel model;

    public TankBER(BlockEntityRendererProvider.Context ctx) {
        model = new FluidModel(ctx.bakeLayer(FluidModel.LAYER_LOCATION));
    }

    public void render(TankBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.getFluidTank().isEmpty() && pBlockEntity.getFluidTexture() != null) {
            pPoseStack.pushPose();
            float i = (float) (pBlockEntity.getFluidTank().getFluidAmount()) / pBlockEntity.getFluidTank().getCapacity();
            pPoseStack.translate(1, 0, 0);
            pPoseStack.scale(1, i, 1);
            pPoseStack.translate(0, -0.5, 0);
            VertexConsumer vertexconsumer = pBufferSource.getBuffer(RenderType.entityCutoutNoCull(pBlockEntity.getFluidTexture()));
            model.renderFluid(pBlockEntity.getFluidTank().getFluid().getFluid(), pPoseStack, vertexconsumer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
            pPoseStack.popPose();
        }
    }
}
