package mod.syconn.swe.client.renders.ber;

import com.mojang.blaze3d.vertex.*;
import mod.syconn.swe.blockentities.TankBE;
import mod.syconn.swe.client.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.neoforge.fluids.FluidStack;

public class TankBER implements BlockEntityRenderer<TankBE> {

    public TankBER(BlockEntityRendererProvider.Context ctx) { }

    public void render(TankBE pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.getFluidTank().isEmpty()) {
            FluidStack fluidStack = pBlockEntity.getFluidTank().getFluidInTank(0);
            float height = (float) (pBlockEntity.getFluidTank().getFluidAmount()) / pBlockEntity.getFluidTank().getCapacity() * 0.9999f;
            pPoseStack.pushPose();
            RenderUtil.renderLiquid(pPoseStack, pBufferSource, fluidStack.getFluid(), 0.02f, 0.98f, height);
            pPoseStack.popPose();
        }
    }
}
