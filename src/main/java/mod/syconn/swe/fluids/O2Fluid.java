package mod.syconn.swe.fluids;

import mod.syconn.swe.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class O2Fluid extends FlowingFluid {

    public Fluid getFlowing() {
        return Registration.O2_FLOWING.get();
    }

    public Fluid getSource() {
        return Registration.O2_SOURCE.get();
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

    public int getAmount(FluidState pState) {
        return 0;
    }

    public Item getBucket() {
        return Registration.O2_BUCKET.get();
    }

    protected boolean canBeReplacedWith(FluidState pState, BlockGetter pLevel, BlockPos pPos, Fluid pFluid, Direction pDirection) {
        return pDirection == Direction.DOWN && !pFluid.is(FluidTags.WATER);
    }

    public int getTickDelay(LevelReader pLevel) {
        return 5;
    }

    protected float getExplosionResistance() {
        return 100.0F;
    }

    protected BlockState createLegacyBlock(FluidState pState) {
        return Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(pState)));
    }

    public boolean isSource(FluidState pState) {
        return false;
    }

    public static class Flowing extends O2Fluid {
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

    public static class Source extends O2Fluid {
        public int getAmount(FluidState pState) {
            return 8;
        }

        public boolean isSource(FluidState pState) {
            return true;
        }
    }
}
