package mod.syconn.swe.services;

import mod.syconn.swe.platform.services.ISingleFluidHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class NeoSingleFluidHandler implements ISingleFluidHandler {

    private IFluidHandler handler;

    public NeoSingleFluidHandler() {}

    public NeoSingleFluidHandler(IFluidHandler handler) {
        this.handler = handler;
    }

    public ISingleFluidHandler get(ItemStack stack) {
        return new NeoSingleFluidHandler(stack.getCapability(Capabilities.FluidHandler.ITEM));
    }

    public FluidHolder getFluidInTank() {
        return of(handler.getFluidInTank(0));
    }

    public int getTankCapacity() {
        return handler.getTankCapacity(0);
    }

    public int fill(FluidHolder resource, FluidAction action) {
        return handler.fill(of(resource), of(action));
    }

    public FluidHolder drain(FluidHolder resource, FluidAction action) {
        return of(handler.drain(of(resource), of(action)));
    }

    public FluidHolder drain(int drain, FluidAction action) {
        return of(handler.drain(drain, of(action)));
    }

    private FluidHolder of(FluidStack stack) {
        return new FluidHolder(stack.getFluid(), stack.getAmount());
    }

    private FluidStack of(FluidHolder holder) {
        return new FluidStack(holder.fluid(), holder.amount());
    }

    private IFluidHandler.FluidAction of(FluidAction action) {
        return switch (action) {
            case EXECUTE -> IFluidHandler.FluidAction.EXECUTE;
            case SIMULATE -> IFluidHandler.FluidAction.SIMULATE;
        };
    }
}
