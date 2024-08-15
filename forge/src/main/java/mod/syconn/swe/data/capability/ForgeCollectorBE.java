package mod.syconn.swe.data.capability;

import mod.syconn.swe.blockentities.CollectorBE;
import mod.syconn.swe.extra.core.InteractionalFluidHandler;
import mod.syconn.swe.wrapper.BlockFluidWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ForgeCollectorBE extends CollectorBE {

    private LazyOptional<IFluidHandler> fluidHandler;
    private LazyOptional<InteractionalFluidHandler> interactionHandler;

    public ForgeCollectorBE(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        if(this.fluidHandler != null) {
            LazyOptional<?> oldHandler = this.fluidHandler;
            this.fluidHandler = null;
            oldHandler.invalidate();
        }
        if(this.interactionHandler != null) {
            LazyOptional<?> oldHandler = this.interactionHandler;
            this.interactionHandler = null;
            oldHandler.invalidate();
        }
    }

    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(!this.remove && cap == ForgeCapabilities.FLUID_HANDLER) {
            if(this.fluidHandler == null) this.fluidHandler = LazyOptional.of(() -> new BlockFluidWrapper(getFluidTank()));
            return this.fluidHandler.cast();
        }
        if(!this.remove && cap == APICapabilities.INTERACTIONAL_HANDLER) {
            if(this.interactionHandler == null) this.interactionHandler = LazyOptional.of(this::getFluidTank);
            return this.interactionHandler.cast();
        }
        return super.getCapability(cap, side);
    }
}
