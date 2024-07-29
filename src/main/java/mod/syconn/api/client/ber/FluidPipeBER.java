package mod.syconn.api.client.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.api.blockEntity.BaseFluidPipeBE;
import mod.syconn.swe.client.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidPipeBER implements BlockEntityRenderer<BaseFluidPipeBE> {

    public FluidPipeBER(BlockEntityRendererProvider.Context ctx) {}

    public void render(BaseFluidPipeBE pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.hasFluid()) {
            FluidStack fluidStack = pBlockEntity.getFluid();
            for (Direction direction : Direction.values()) {
                RenderUtil.renderFluidInPipe(pPoseStack, pBufferSource, fluidStack.getFluid(), pBlockEntity.getConnectionType(direction), direction);
            }
        }
    }
}
