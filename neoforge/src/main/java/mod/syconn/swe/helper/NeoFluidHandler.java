package mod.syconn.swe.helper;

import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.core.FluidHolder;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class NeoFluidHandler {

    public static class BlockFluidHandler implements FluidHandler {

        private final IFluidHandler handler;

        public BlockFluidHandler(IFluidHandler handler) {
            this.handler = handler;
        }

        public FluidHolder getFluidHolder() {
            return of(handler.getFluidInTank(0));
        }

        public int getTankCapacity() {
            return handler.getTankCapacity(0);
        }

        public void setFluid(FluidHolder fluidHolder) {
            onContentsChanged();
        }

        public boolean isFluidValid(FluidHolder holder) {
            return handler.isFluidValid(0, of(holder));
        }

        public void onContentsChanged() { }

        public int fill(FluidHolder resource, FluidAction doFill) {
            return handler.fill(of(resource), of(doFill));
        }

        public FluidHolder drain(FluidHolder resource, FluidAction action) {
            return of(handler.drain(of(resource), of(action)));
        }

        public FluidHolder drain(int maxDrain, FluidAction action) {
            return of(handler.drain(maxDrain, of(action)));
        }
    }

    public static class ItemFluidHandler implements FluidHandlerItem {

        private final IFluidHandlerItem handler;

        public ItemFluidHandler(IFluidHandlerItem handler) {
            this.handler = handler;
        }

        public FluidHolder getFluidHolder() {
            return of(handler.getFluidInTank(0));
        }

        public int getTankCapacity() {
            return handler.getTankCapacity(0);
        }

        public void setFluid(FluidHolder fluidHolder) {
            onContentsChanged();
        }

        public boolean isFluidValid(FluidHolder holder) {
            return handler.isFluidValid(0, of(holder));
        }

        public void onContentsChanged() {}

        public int fill(FluidHolder resource, FluidAction doFill) { // TODO MAY NOT WORK CAUSE NOT COMPATIBLE
            return handler.fill(of(resource), of(doFill));
        }

        public FluidHolder drain(FluidHolder resource, FluidAction action) {
            return of(handler.drain(of(resource), of(action)));
        }

        public FluidHolder drain(int maxDrain, FluidAction action) {
            return of(handler.drain(maxDrain, of(action)));
        }

        public ItemStack getContainer() {
            return handler.getContainer();
        }
    }

    private static FluidHolder of(FluidStack stack) {
        return new FluidHolder(stack.getFluid(), stack.getAmount());
    }

    private static FluidStack of(FluidHolder holder) {
        return new FluidStack(holder.getFluid(), holder.getAmount());
    }

    private static IFluidHandler.FluidAction of(FluidAction action) {
        return switch (action) {
            case EXECUTE -> IFluidHandler.FluidAction.EXECUTE;
            case SIMULATE -> IFluidHandler.FluidAction.SIMULATE;
        };
    }
}
