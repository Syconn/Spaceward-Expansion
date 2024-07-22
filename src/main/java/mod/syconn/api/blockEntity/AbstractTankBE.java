package mod.syconn.api.blockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTankBE extends SyncedBlockEntity {

    protected FluidTank tank;
    private final Lazy<IFluidHandler> holder = Lazy.of(() -> tank);

    public AbstractTankBE(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int size) {
        super(blockEntityType, pos, state);
        tank = new FluidTank(size) {
            protected void onContentsChanged() {
                markDirty();
            }
        };
    }

    protected void loadClientData(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        tank.readFromNBT(pRegistries, pTag);
    }

    protected void saveClientData(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        tank.writeToNBT(pRegistries, pTag);
    }

    public FluidTank getFluidTank() {
        return tank;
    }

    public IFluidHandler getFluidHandler() {
        return holder.get();
    }
}
