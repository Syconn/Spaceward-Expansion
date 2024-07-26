package mod.syconn.api.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.api.blockEntity.AbstractPipeBE;
import mod.syconn.api.blockEntity.BaseFluidPipeBE;
import mod.syconn.api.util.PipeConnectionTypes;
import mod.syconn.api.world.data.PipeNetwork;
import mod.syconn.api.world.data.savedData.PipeNetworks;
import mod.syconn.swe.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BaseFluidPipe extends AbstractPipeBlock {

    public BaseFluidPipe(Properties properties) {
        super(properties);
    }

    protected PipeConnectionTypes getConnectorType(BlockGetter level, BlockPos thisPos, BlockPos connectionPos, Direction facing) {
        return level.getBlockEntity(connectionPos) instanceof AbstractPipeBE ? PipeConnectionTypes.CABLE : PipeConnectionTypes.NONE;
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel instanceof ServerLevel sl) {
//            PipeNetworks.get(sl).fixList();
            System.out.println(PipeNetworks.get(sl).getDataMap());
        }
        return InteractionResult.PASS;
    }

    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if (pLevel instanceof ServerLevel sl && !(pOldState.getBlock() instanceof AbstractPipeBlock) && pLevel.getBlockEntity(pPos) instanceof AbstractPipeBE be)
            be.setNetworkID(PipeNetworks.get(sl).addPipe(pPos));
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel instanceof ServerLevel sl && !(pNewState.getBlock() instanceof AbstractPipeBlock)) PipeNetworks.get(sl).removePipe(pPos);
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return Registration.FLUID_TANK_CODEC.value();
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BaseFluidPipeBE(pPos, pState);
    }
}
