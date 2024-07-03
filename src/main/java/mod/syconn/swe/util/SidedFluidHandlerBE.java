package mod.syconn.swe.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SidedFluidHandlerBE extends BlockEntity {

    protected FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME);

    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);
    private final List<Direction> side;

    public SidedFluidHandlerBE(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, List<Direction> side)
    {
        super(blockEntityType, pos, state);
        this.side = side;
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        tank.readFromNBT(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tank.writeToNBT(tag);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == ForgeCapabilities.FLUID_HANDLER && (facing == null || (side == null || side.contains(facing))))
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    protected static List<Direction> allSides() {
        return Arrays.stream(Direction.values()).toList();
    }

    protected static List<Direction> topOrBottom() {
        return List.of(Direction.UP, Direction.DOWN);
    }
}
