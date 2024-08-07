package mod.syconn.swe.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import mod.syconn.swe.fluids.FluidStorageBlock;

public abstract class FluidBaseTopperBlock extends BaseEntityBlock {

    public FluidBaseTopperBlock(Properties p_49224_) {
        super(p_49224_);
    }

    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        return p_60526_.getBlockState(p_60527_.below()).getBlock() instanceof FluidStorageBlock;
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    public BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return !canSurvive(p_60541_, p_60544_, p_60545_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);
    }
}
