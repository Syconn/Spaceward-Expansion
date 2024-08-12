package mod.syconn.swe.client.renders.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import mod.syconn.swe.blockentities.CanisterFillerBlockEntity;

public class CanisterBER implements BlockEntityRenderer<CanisterFillerBlockEntity> {

    private final ItemRenderer renderer;

    public CanisterBER(BlockEntityRendererProvider.Context ctx) {
        renderer = ctx.getItemRenderer();
    }

    public void render(CanisterFillerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        boolean north = pBlockEntity.getBlockState().getValue(BlockStateProperties.FACING) == Direction.NORTH || pBlockEntity.getBlockState().getValue(BlockStateProperties.FACING) == Direction.SOUTH;
        for (int i = 0; i < 4; i++) {
            if (!pBlockEntity.getCanister(i).isEmpty()) {
                double row = .1;
                if (i == 1) row = .35;
                else if (i == 2) row = .65;
                else if (i == 3) row = .9;
                pPoseStack.pushPose();
                pPoseStack.translate(!north ? row : .5, 0.45, north ? row : .5);
                if (!north) pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
                int j = LevelRenderer.getLightColor(pBlockEntity.getLevel(), pBlockEntity.getBlockPos());
                renderer.renderStatic(pBlockEntity.getCanister(i), ItemDisplayContext.GROUND, j, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, pBlockEntity.getLevel(), 0);
                pPoseStack.popPose();
            }
        }
    }
}
