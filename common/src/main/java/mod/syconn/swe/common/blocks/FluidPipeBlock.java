package mod.syconn.swe.common.blocks;

import mod.syconn.swe.client.screen.FluidPipeScreen;
import mod.syconn.swe.common.blockentities.AbstractPipeBE;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.blockentities.FluidPipeBE;
import mod.syconn.swe.server.savedData.PipeNetworks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static mod.syconn.swe.util.PipeUtil.PipeConnectionTypes;

public class FluidPipeBlock extends AbstractPipeBlock {

    public FluidPipeBlock(Properties properties) {
        super(properties);
    }

    protected PipeConnectionTypes getConnectorType(BlockGetter level, BlockPos thisPos, BlockPos connectionPos, Direction facing) {
        if (FluidHolderBlock.hasHolder((Level) level, connectionPos, facing.getOpposite())) return PipeConnectionTypes.BLOCK;
        return level.getBlockEntity(connectionPos) instanceof AbstractPipeBE ? PipeConnectionTypes.CABLE : PipeConnectionTypes.NONE;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof FluidPipeBE pipeBE && pipeBE.hasMenu()) {
            if (level.isClientSide()) Minecraft.getInstance().setScreen(new FluidPipeScreen(pipeBE));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
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

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FluidPipeBE(pPos, pState);
    }
}
