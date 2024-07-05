package mod.syconn.swe.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import mod.syconn.swe.fluids.FluidStorageBlock;
import mod.syconn.swe.blockentities.TankBlockEntity;
import mod.syconn.swe.util.FluidHelper;

public class FluidTank extends FluidBaseBlock implements FluidStorageBlock {

    public FluidTank() {
        super(Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion());
    }

    public InteractionResult use(BlockState state, Level l, BlockPos pos, Player p, InteractionHand p_48710_, BlockHitResult p_48711_) {
        if (l.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if(FluidUtil.interactWithFluidHandler(p, p_48710_, l, pos, p_48711_.getDirection())) {
                return InteractionResult.SUCCESS;
            }
            else if (FluidHelper.interactWithFluidHandler(p.getItemInHand(p_48710_), l, pos, null)) {
                return InteractionResult.SUCCESS;
            }
            BlockEntity blockentity = l.getBlockEntity(pos);
            if (blockentity instanceof TankBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) p, (MenuProvider) blockentity, pos);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return createTickerHelper(p_153214_, ModBlockEntity.TANK.get(), TankBlockEntity::serverTick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TankBlockEntity(pos, state);
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
