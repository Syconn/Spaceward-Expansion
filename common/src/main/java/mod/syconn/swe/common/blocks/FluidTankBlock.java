package mod.syconn.swe.common.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.common.blockentities.TankBE;
import mod.syconn.swe.core.ModBlockEntities;
import mod.syconn.swe.core.ModBlocks;
import mod.syconn.swe.network.Network;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FluidTankBlock extends BaseEntityBlock {

    public FluidTankBlock(Properties properties) {
        super(properties);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;
        if (pPlayer instanceof ServerPlayer sp && pLevel.getBlockEntity(pPos) instanceof TankBE tankBE) {
            Network.openMenuWithData(sp, tankBE, new PositionMenuData(pPos));
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }

    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (Services.FLUID_HANDLER.has(pStack) && Services.FLUID_HELPER.interactWithBlock(pLevel, pPos, pHitResult, pPlayer, pHand)) return ItemInteractionResult.CONSUME;
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return createTickerHelper(p_153214_, ModBlockEntities.TANK.get(), TankBE::serverTick);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TankBE(pos, state);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return ModBlocks.FLUID_TANK_CODEC.get();
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level l, BlockPos pos) {
        if (l.getBlockEntity(pos) instanceof TankBE te) {
            double o = (double) (te.getFluidTank().getFluidHolder().getAmount()) / te.getFluidTank().getTankCapacity();
            return (int) (o * 15);
        }
        return 0;
    }
}
