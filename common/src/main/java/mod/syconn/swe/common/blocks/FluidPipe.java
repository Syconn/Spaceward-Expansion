package mod.syconn.swe.common.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.blockentities.FluidPipeBE;
import mod.syconn.swe.blockentities.base.AbstractPipeBE;
import mod.syconn.swe.common.blocks.base.AbstractPipeBlock;
import mod.syconn.swe.extra.data.savedData.PipeNetworks;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.core.ModBlocks;
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

import static mod.syconn.swe.extra.PipePatterns.PipeConnectionTypes;

public class FluidPipe extends AbstractPipeBlock {

    public FluidPipe(Properties properties) {
        super(properties);
    }

    protected PipeConnectionTypes getConnectorType(BlockGetter level, BlockPos thisPos, BlockPos connectionPos, Direction facing) {
        if (Services.FLUID_HANDLER.has(level, connectionPos, facing.getOpposite())) return PipeConnectionTypes.BLOCK;
        return level.getBlockEntity(connectionPos) instanceof AbstractPipeBE ? PipeConnectionTypes.CABLE : PipeConnectionTypes.NONE;
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof FluidPipeBE pipeBE && pipeBE.hasMenu()) {
            if (pLevel.isClientSide()) ClientHooks.openPipeScreen(pipeBE);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if (pLevel instanceof ServerLevel sl && !(pOldState.getBlock() instanceof AbstractPipeBlock) && pLevel.getBlockEntity(pPos) instanceof AbstractPipeBE be)
            be.setNetworkID(PipeNetworks.get(sl).addPipe(pPos));
        if (pLevel instanceof ServerLevel sl && pOldState.getBlock() instanceof AbstractPipeBlock && pLevel.getBlockEntity(pPos) instanceof AbstractPipeBE be)
            PipeNetworks.get(sl).updatePipe(be.getNetworkID(), pPos);
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel instanceof ServerLevel sl && !(pNewState.getBlock() instanceof AbstractPipeBlock) && pLevel.getBlockEntity(pPos) instanceof AbstractPipeBE be)
            PipeNetworks.get(sl).removePipe(be.getNetworkID(), pPos);
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return ModBlocks.FlUID_PIPE_CODEC.get();
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FluidPipeBE(pPos, pState);
    }
}
