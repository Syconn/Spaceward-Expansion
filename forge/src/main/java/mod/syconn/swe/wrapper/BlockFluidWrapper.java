package mod.syconn.swe.wrapper;

import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHolder;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockFluidWrapper implements IFluidHandler {

    private final FluidHandler handler;

    public BlockFluidWrapper(FluidHandler handler) {
        this.handler = handler;
    }

    public int getTanks() {
        return 1;
    }

    public FluidStack getFluidInTank(int tank) {
        return of(handler.getFluid());
    }

    public int getTankCapacity(int tank) {
        return handler.getCapacity();
    }

    public boolean isFluidValid(int tank, FluidStack stack) {
        return handler.isFluidValid(of(stack));
    }

    public int fill(FluidStack resource, FluidAction action) {
        return handler.fill(of(resource), of(action));
    }

    public FluidStack drain(FluidStack resource, FluidAction action) {
        return of(handler.drain(of(resource), of(action)));
    }

    public FluidStack drain(int maxDrain, FluidAction action) {
        return of(handler.drain(maxDrain, of(action)));
    }

    private FluidStack of(FluidHolder holder) {
        return new FluidStack(holder.getFluid(), holder.getAmount());
    }

    private FluidHolder of(FluidStack holder) {
        return new FluidHolder(holder.getFluid(), holder.getAmount());
    }

    private mod.syconn.swe.extra.core.FluidAction of(FluidAction action) {
        return switch (action) {
            case EXECUTE -> mod.syconn.swe.extra.core.FluidAction.EXECUTE;
            case SIMULATE -> mod.syconn.swe.extra.core.FluidAction.SIMULATE;
        };
    }
}
