package mod.syconn.swe.fluids;

import mod.syconn.swe.Constants;
import mod.syconn.swe.init.BlockRegister;
import mod.syconn.swe.init.FluidRegister;
import mod.syconn.swe.init.ItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class OxygenFlowingFluid extends FlowingFluid {

    public static final ResourceLocation O2_STILL_RL = Constants.loc("block/o2_still");
    public static final ResourceLocation O2_FLOWING_RL = Constants.loc("block/o2_flowing");
    public static final ResourceLocation O2_OVERLAY_RL = Constants.loc("block/o2_overlay.png");

    public Fluid getFlowing() {
        return FluidRegister.O2_FLOWING.get();
    }

    public Fluid getSource() {
        return FluidRegister.O2.get();
    }

    public Item getBucket() {
        return ItemRegister.O2_BUCKET.get();
    }

    protected boolean canConvertToSource(Level pLevel) {
        return false;
    }

    protected void beforeDestroyingBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        BlockEntity blockentity = pState.hasBlockEntity() ? pLevel.getBlockEntity(pPos) : null;
        Block.dropResources(pState, pLevel, pPos, blockentity);
    }

    protected int getSlopeFindDistance(LevelReader pLevel) {
        return 4;
    }

    protected int getDropOff(LevelReader pLevel) {
        return 1;
    }

    protected boolean canBeReplacedWith(FluidState pState, BlockGetter pLevel, BlockPos pPos, Fluid pFluid, Direction pDirection) {
        return pDirection == Direction.DOWN && !isSame(pFluid);
    }

    public int getTickDelay(LevelReader pLevel) {
        return 5;
    }

    protected float getExplosionResistance() {
        return 1;
    }

    protected BlockState createLegacyBlock(FluidState pState) {
        return BlockRegister.O2.get().defaultBlockState().setValue(LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(pState)));
    }

    public static class Flowing extends OxygenFlowingFluid {

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> pBuilder) {
            super.createFluidStateDefinition(pBuilder);
            pBuilder.add(LEVEL);
        }

        public int getAmount(FluidState pState) {
            return pState.getValue(LEVEL);
        }

        public boolean isSource(FluidState pState) {
            return false;
        }
    }

    public static class Source extends OxygenFlowingFluid {

        public int getAmount(FluidState pState) {
            return 8;
        }

        public boolean isSource(FluidState pState) {
            return true;
        }
    }
}
