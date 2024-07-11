package mod.syconn.swe.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class SidedFluidHandlerBE extends BlockEntity {

    protected FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME);
    private final Lazy<IFluidHandler> holder = Lazy.of(() -> tank);

    public SidedFluidHandlerBE(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        tank.readFromNBT(pRegistries, pTag);
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        tank.writeToNBT(pRegistries, pTag);
    }

    public FluidTank getTank() {
        return tank;
    }

    public IFluidHandler getFluidHandler() {
        return holder.get();
    }
}
