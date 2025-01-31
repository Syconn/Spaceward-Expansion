package mod.syconn.swe.util;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.architectury.platform.Platform;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidHolderWrapper extends FluidHolderBlock {
    private final IFluidHandler fluidHandler;
    private final int tank;

    public FluidHolderWrapper(IFluidHandler fluidHandler, int tank) {
        this.fluidHandler = fluidHandler;
        this.tank = tank;
    }

    public FluidStack getFluidStack() {
        return FluidStackHooksForge.fromForge(fluidHandler.getFluidInTank(tank));
    }

    public boolean isEmpty() {
        return fluidHandler.getFluidInTank(tank).isEmpty();
    }

    public void setFluidStack(FluidStack fluidStack) {
        if (Platform.isDevelopmentEnvironment()) throw new AssertionError("Unexpected Result"); // TODO REMOVE IF NOT NEEDED :)
    }

    public long push(FluidStack fluidStack, boolean simulate) {
        return fluidHandler.fill(FluidStackHooksForge.toForge(fluidStack), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    public FluidStack pull(long amount, boolean simulate) {
        return FluidStackHooksForge.fromForge(fluidHandler.drain((int) amount, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    public void load(CompoundTag tag) { }

    public void save(CompoundTag tag) { }
}
