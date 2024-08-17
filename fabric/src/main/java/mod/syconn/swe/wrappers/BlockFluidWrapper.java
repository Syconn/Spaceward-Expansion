package mod.syconn.swe.wrappers;

import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class BlockFluidWrapper implements SingleSlotStorage<FluidVariant> {

    private final FluidHandler handler;

    public BlockFluidWrapper(FluidHandler handler) {
        this.handler = handler;
    }

    public FluidVariant getResource() {
        return FluidVariant.of(handler.getFluidHolder().getFluid());
    }

    public long getAmount() {
        return handler.getFluidAmount();
    }

    public long getCapacity() {
        return handler.getTankCapacity();
    }

    public FluidHandler getHandler() {
        return handler;
    }

    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return handler.fill(new FluidHolder(resource.getFluid(), (int) maxAmount), FluidAction.EXECUTE);
    }

    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return handler.drain(new FluidHolder(resource.getFluid(), (int) maxAmount), FluidAction.EXECUTE).getAmount();
    }

    public boolean isResourceBlank() {
        return handler.getFluidHolder().isEmpty();
    }

    public String toString() {
        return "BlockFluidWrapper[%d %s]".formatted(handler.getFluidAmount(), handler.getFluidHolder().getFluid());
    }
}
