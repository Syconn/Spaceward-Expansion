package mod.syconn.swe.blockentities;

import mod.syconn.swe.blockentities.base.AbstractPipeBE;
import mod.syconn.swe.blocks.BaseFluidPipe;
import mod.syconn.swe.blocks.base.AbstractPipeBlock;
import mod.syconn.swe.extra.PipePatterns;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.init.BlockEntityRegister;
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

    private Fluid fluid = null;

    public FluidPipeBE(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.PIPE.get(), pos, state);
    }

    public boolean canConnectToPipe(Level level, BlockPos pos, Direction conDir) {
        return level.getBlockState(pos.relative(conDir)).getBlock() instanceof BaseFluidPipe;
    }

    public boolean hasMenu() {
        for (Direction direction : Direction.values()) if (getConnectionType(direction).isInteractionPoint()) return true;
        return false;
    }

    public PipePatterns.PipeConnectionTypes getConnectionType(Direction direction) {
        return getBlockState().getValue(AbstractPipeBlock.fromDirection(direction));
    }

    public void setConnectionType(Direction direction, PipePatterns.PipeConnectionTypes type) {
        level.setBlock(worldPosition, getBlockState().setValue(AbstractPipeBlock.fromDirection(direction), type), 2);
    }

    protected void saveClientData(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveClientData(tag, pRegistries);
        if (fluid != null) tag.putInt("fluid", BuiltInRegistries.FLUID.getId(fluid));
    }

    protected void loadClientData(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadClientData(tag, pRegistries);
        if (tag.contains("fluid")) fluid = BuiltInRegistries.FLUID.byId(tag.getInt("fluid"));
    }

    public void setFluid(@Nullable Fluid fluid) {
        this.fluid = fluid;
        markDirty();
    }

    public boolean hasFluid() {
        return fluid != null && !fluid.isSame(Fluids.EMPTY);
    }

    public FluidHolder getFluid() {
        return new FluidHolder(fluid, 1000);
    }
}
