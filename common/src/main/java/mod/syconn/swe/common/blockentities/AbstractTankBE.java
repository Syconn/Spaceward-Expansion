package mod.syconn.swe.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTankBE extends BlockEntity {

    protected InteractableFluidTank tank;

    public AbstractTankBE(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int size, int speed) {
        super(blockEntityType, pos, state);
        tank = new InteractableFluidTank(size, speed) {
            public void onContentsChanged() { markDirty(); }
        };
    }



    public InteractableFluidTank getFluidTank() {
        return tank;
    }
}
