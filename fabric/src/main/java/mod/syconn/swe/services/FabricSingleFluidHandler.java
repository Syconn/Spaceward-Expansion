package mod.syconn.swe.services;

import mod.syconn.swe.platform.services.ISingleFluidHandler;
import mod.syconn.swe.wrappers.ComponentFluidWrapper;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;

public class FabricSingleFluidHandler implements ISingleFluidHandler {

    private ComponentFluidWrapper handler;

    public FabricSingleFluidHandler() {}

    public FabricSingleFluidHandler(ComponentFluidWrapper handler) {
        this.handler = handler;
    }

    public ISingleFluidHandler get(ItemStack stack) {
        return new FabricSingleFluidHandler((ComponentFluidWrapper) ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM));
    }

    public FluidHolder getFluidInTank() {
        return handler.getFluid();
    }

    public int getTankCapacity() {
        return (int) handler.getCapacity();
    }

    public int fill(FluidHolder resource, FluidAction action) {
        return (int) handler.insert(FluidVariant.of(resource.fluid()), resource.amount(), Transaction.openOuter());
    }

    public FluidHolder drain(FluidHolder resource, FluidAction action) {
        return new FluidHolder(resource.fluid(), (int) handler.extract(FluidVariant.of(resource.fluid()), resource.amount(), Transaction.openOuter()));
    }

    public FluidHolder drain(int drain, FluidAction action) {
        return new FluidHolder(handler.getFluid().fluid(), (int) handler.extract(FluidVariant.of(handler.getFluid().fluid()), drain, Transaction.openOuter()));
    }
}
