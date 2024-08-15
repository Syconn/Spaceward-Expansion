package mod.syconn.swe.data.capability;

import mod.syconn.swe.blockentities.TankBE;
import mod.syconn.swe.extra.core.InteractionalFluidHandler;
import mod.syconn.swe.wrapper.BlockFluidWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ForgeTankBE extends TankBE {

    private LazyOptional<IFluidHandler> fluidHandler;
    private LazyOptional<InteractionalFluidHandler> interactionHandler;
    private LazyOptional<IItemHandler> itemHandler;

    public ForgeTankBE(BlockPos pos, BlockState state) {
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
        if(this.itemHandler != null) {
            LazyOptional<?> oldHandler = this.itemHandler;
            this.itemHandler = null;
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
        if(!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if(this.itemHandler == null) this.itemHandler = LazyOptional.of(() -> new InvWrapper(this));
            return this.itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }
}
