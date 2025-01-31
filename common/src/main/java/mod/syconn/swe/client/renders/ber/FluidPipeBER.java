package mod.syconn.swe.client.renders.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swe.common.blockentities.FluidPipeBE;
import mod.syconn.swe.util.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class FluidPipeBER implements BlockEntityRenderer<FluidPipeBE> {

    public FluidPipeBER(BlockEntityRendererProvider.Context ctx) {}

    public void render(FluidPipeBE pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.hasFluid()) {
            FluidHolder fluidHolder = pBlockEntity.getFluid();
            for (Direction direction : Direction.values()) {
                RenderUtil.renderFluidInPipe(pPoseStack, pBufferSource, fluidHolder.getFluid(), pBlockEntity.getConnectionType(direction), direction);
            }
        }
    }
}
