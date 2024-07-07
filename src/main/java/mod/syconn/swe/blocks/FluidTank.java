package mod.syconn.swe.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.Registration;
import mod.syconn.swe.blockentities.TankBlockEntity;
import mod.syconn.swe.fluids.FluidStorageBlock;
import mod.syconn.swe.util.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
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

    public InteractionResult use(BlockState state, Level l, BlockPos pos, Player p, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!l.isClientSide) {
            if (FluidUtil.interactWithFluidHandler(p, pHand, l, pos, pHitResult.getDirection())) return InteractionResult.SUCCESS;
            else if (FluidHelper.interactWithFluidHandler(p.getItemInHand(pHand), l, pos, null)) return InteractionResult.SUCCESS;
            BlockEntity blockentity = l.getBlockEntity(pos);
            if (blockentity instanceof TankBlockEntity) {
                p.openMenu((MenuProvider) blockentity, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
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
