package mod.syconn.swe.common.blockentities.base;

import mod.syconn.swe.extra.core.InteractableFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTankBE extends SyncedBE {

    protected InteractableFluidTank tank;

    public AbstractTankBE(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int size, int speed) {
        super(blockEntityType, pos, state);
        tank = new InteractableFluidTank(size, speed) {
            public void onContentsChanged() { markDirty(); }
        };
    }

    protected void loadClientData(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        tank.readNBT(pRegistries, pTag);
    }

    protected void saveClientData(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        tank.writeNBT(pRegistries, pTag);
    }

    public InteractableFluidTank getFluidTank() {
        return tank;
    }
}
