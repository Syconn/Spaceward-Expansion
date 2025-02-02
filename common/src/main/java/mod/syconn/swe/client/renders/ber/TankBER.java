package mod.syconn.swe.client.renders.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.blockentities.TankBE;
import mod.syconn.swe.util.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class TankBER implements BlockEntityRenderer<TankBE> {

    public TankBER(BlockEntityRendererProvider.Context ctx) { }

    public void render(TankBE pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.getFluidHolder().isEmpty()) {
            FluidHolderBlock fluidHolder = pBlockEntity.getFluidHolder();
            Direction[] directions;
            float height = (float) (fluidHolder.getFluidStack().getAmount()) / fluidHolder.getCapacity();
            if (height < 0.9) directions = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP};
            else directions = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
            pPoseStack.pushPose();
            pPoseStack.translate(0.0001, 0.0001, 0.0001);
            pPoseStack.scale(0.9998f, 0.9999f * height, 0.9998f);
            RenderUtil.renderLiquid(pPoseStack, pBufferSource, fluidHolder.getFluidStack().getFluid(), directions);
            pPoseStack.popPose();
        }
    }
}
