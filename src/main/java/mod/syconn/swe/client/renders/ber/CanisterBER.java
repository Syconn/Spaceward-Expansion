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

    @Override
    public void render(CanisterFillerBlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        boolean north = p_112307_.getBlockState().getValue(BlockStateProperties.FACING) == Direction.NORTH || p_112307_.getBlockState().getValue(BlockStateProperties.FACING) == Direction.SOUTH;
        for (int i = 0; i < 4; i++) {
            if (!p_112307_.getCanister(i).isEmpty()) {
                double row = .1;
                if (i == 1) row = .35;
                else if (i == 2) row = .65;
                else if (i == 3) row = .9;
                p_112309_.pushPose();
                p_112309_.translate(!north ? row : .5, 0.45, north ? row : .5);
                if (!north) p_112309_.mulPose(Axis.YP.rotationDegrees(90));
                int j = LevelRenderer.getLightColor(p_112307_.getLevel(), p_112307_.getBlockPos());
                renderer.renderStatic(p_112307_.getCanister(i), ItemDisplayContext.GROUND, j, OverlayTexture.NO_OVERLAY, p_112309_, p_112310_, p_112307_.getLevel(), 0);
                p_112309_.popPose();
            }
        }
    }
}
