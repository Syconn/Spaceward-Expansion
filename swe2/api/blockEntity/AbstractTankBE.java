package mod.syconn.swe2.api.blockEntity;

import mod.syconn.swe2.api.world.data.capability.IFluidHandlerInteractable;
import mod.syconn.swe2.api.world.data.capability.InteractableFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTankBE extends SyncedBE {

    protected InteractableFluidTank tank;
    private final Lazy<IFluidHandlerInteractable> holder = Lazy.of(() -> tank);

    public AbstractTankBE(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int size, int speed) {
        super(blockEntityType, pos, state);
        tank = new InteractableFluidTank(size, speed) {
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

    public InteractableFluidTank getFluidTank() {
        return tank;
    }

    public IFluidHandlerInteractable getFluidHandler() {
        return holder.get();
    }
}
