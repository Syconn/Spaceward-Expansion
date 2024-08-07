package mod.syconn.swe.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.Registration;
import mod.syconn.swe.blockentities.TankBE;
import mod.syconn.swe.fluids.FluidStorageBlock;
import mod.syconn.swe.util.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidTank extends FluidBaseBlock implements FluidStorageBlock {

    public FluidTank(Properties properties) {
        super(properties);
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;
        if (pLevel.getBlockEntity(pPos) instanceof TankBE tankBE) {
            pPlayer.openMenu(tankBE, pPos);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }

    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pStack.getCapability(Capabilities.FluidHandler.ITEM) != null && FluidHelper.maxTransferStackToBlock(pLevel, pPos, null, pStack).isSuccess()) return ItemInteractionResult.CONSUME;
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return createTickerHelper(p_153214_, Registration.TANK.get(), TankBE::serverTick);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TankBE(pos, state);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return Registration.FLUID_TANK_CODEC.value();
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level l, BlockPos pos) {
        if (l.getBlockEntity(pos) instanceof TankBE te) {
            double o = (double) (te.getFluidTank().getFluidAmount()) / te.getFluidTank().getCapacity();
            return (int) (o * 15);
        }
        return 0;
    }
}
