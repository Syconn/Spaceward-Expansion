package mod.syconn.swe.common.blockentities;

import mod.syconn.swe.common.blocks.FluidPipeBlock;
import mod.syconn.swe.common.blocks.AbstractPipeBlock;
import mod.syconn.swe.core.ModBlockEntities;
import mod.syconn.swe.util.PipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class FluidPipeBE extends AbstractPipeBE {

    private Fluid fluid = Fluids.EMPTY;

    public FluidPipeBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIPE.get(), pos, state);
    }

    public boolean canConnectToPipe(Level level, BlockPos pos, Direction conDir) {
        return level.getBlockState(pos.relative(conDir)).getBlock() instanceof FluidPipeBlock;
    }

    public boolean hasMenu() {
        for (Direction direction : Direction.values()) if (getConnectionType(direction).isInteractionPoint()) return true;
        return false;
    }

    public PipeUtil.PipeConnectionTypes getConnectionType(Direction direction) {
        return getBlockState().getValue(AbstractPipeBlock.fromDirection(direction));
    }

    public void setConnectionType(Direction direction, PipeUtil.PipeConnectionTypes type) {
        level.setBlock(worldPosition, getBlockState().setValue(AbstractPipeBlock.fromDirection(direction), type), 2);
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (fluid != null) tag.putInt("fluid", BuiltInRegistries.FLUID.getId(fluid));
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("fluid")) fluid = BuiltInRegistries.FLUID.byId(tag.getInt("fluid"));
    }

    public void setFluid(@Nullable Fluid fluid) {
        this.fluid = fluid;
        markDirty();
    }

    public boolean hasFluid() {
        return fluid != null && !fluid.isSame(Fluids.EMPTY);
    }

    public Fluid getFluid() {
        return fluid;
    }
}
