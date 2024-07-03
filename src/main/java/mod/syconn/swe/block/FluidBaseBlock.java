package mod.syconn.swe.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class FluidBaseBlock extends BaseEntityBlock {
    public static BooleanProperty FLUID_TYPE = BooleanProperty.create("fluid_type");
    public static BooleanProperty ENABLED = BooleanProperty.create("enabled");

    protected FluidBaseBlock(Properties p_49224_) {
        super(p_49224_);
    }

    public void onPlace(BlockState p_54110_, Level p_54111_, BlockPos p_54112_, BlockState p_54113_, boolean p_54114_) {
        if (!p_54113_.is(p_54110_.getBlock())) {
            this.checkPoweredState(p_54111_, p_54112_, p_54110_, 2);
        }
    }

    public void neighborChanged(BlockState p_54078_, Level p_54079_, BlockPos p_54080_, Block p_54081_, BlockPos p_54082_, boolean p_54083_) {
        this.checkPoweredState(p_54079_, p_54080_, p_54078_, 4);
    }

    protected void checkPoweredState(Level p_275499_, BlockPos p_275298_, BlockState p_275611_, int p_275625_) {
        boolean flag = !p_275499_.hasNeighborSignal(p_275298_);
        if (flag != p_275611_.getValue(ENABLED)) p_275499_.setBlock(p_275298_, p_275611_.setValue(ENABLED, flag), p_275625_);
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> def) {
        def.add(ENABLED);
    }
}
