package mod.syconn.swe.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTankBE extends SyncedBE implements InteractableFluidHolderBlock.IInteractableFluidHolderBlock {

    protected final InteractableFluidHolderBlock tank;

    public AbstractTankBE(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, long speed, long capacity) {
        super(blockEntityType, pos, state);
        tank = InteractableFluidHolderBlock.create(speed, capacity, container -> {
            markDirty();
            container.sync(this);
        });
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.tank.load(tag.getCompound("FluidTank"));
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag tankTag = new CompoundTag();
        this.tank.save(tankTag);
        tag.put("FluidTank", tankTag);
    }

    public InteractableFluidHolderBlock getFluidHolder() {
        return tank;
    }
}
