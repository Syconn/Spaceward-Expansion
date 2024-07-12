package mod.syconn.swe.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.Registration;
import mod.syconn.swe.blockentities.TankBlockEntity;
import mod.syconn.swe.fluids.FluidStorageBlock;
import mod.syconn.swe.util.FluidHelper;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.fluids.FluidUtil;

public class FluidTank extends FluidBaseBlock implements FluidStorageBlock {

    public FluidTank(Properties properties) {
        super(properties);
    }

    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (blockentity instanceof TankBlockEntity) {
            pPlayer.openMenu((MenuProvider) blockentity, pPos);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }

    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide) {
            if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, pHitResult.getDirection())) return ItemInteractionResult.CONSUME;
            else if (FluidHelper.interactWithFluidHandler(pPlayer.getItemInHand(pHand), pLevel, pPos, null)) return ItemInteractionResult.CONSUME;
        }
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return createTickerHelper(p_153214_, Registration.TANK.get(), TankBlockEntity::serverTick);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TankBlockEntity(pos, state);
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return Registration.FLUID_TANK_CODEC.value();
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level l, BlockPos pos) {
        if (l.getBlockEntity(pos) instanceof TankBlockEntity te) {
            double o = (double) (te.getFluidTank().getFluidAmount()) / te.getFluidTank().getCapacity();
            return (int) (o * 15);
        }
        return 0;
    }
}
