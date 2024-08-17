package mod.syconn.swe.wrappers;

import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.data.components.FluidHolderComponent;
import mod.syconn.swe.init.ComponentRegister;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;

public class ComponentFluidWrapper implements FluidHandlerItem, SingleSlotStorage<FluidVariant> {
    protected final ItemStack container;
    protected int capacity;

    public ComponentFluidWrapper(ContainerItemContext context, int capacity) {
        this.container = context.getItemVariant().toStack();
        this.capacity = capacity;
    }

    public FluidHolder getFluidHolder() {
        FluidHolderComponent component = container.getOrDefault(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.EMPTY);
        return component.fluidHolder();
    }

    public void setFluid(FluidHolder fluid) {
        FluidHolderComponent component = container.getOrDefault(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.EMPTY);
        container.set(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.of(fluid.getFluid(), fluid.getAmount(), component.capacity()));
    }

    public int getFluidAmount() {
        FluidHolderComponent component = container.getOrDefault(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.EMPTY);
        return component.fluidHolder().getAmount();
    }

    public int getTankCapacity() {
        return capacity;
    }

    public ItemStack getContainer() {
        return container;
    }

    public boolean isFluidValid(FluidHolder holder) {
        return true;
    }

    public void onContentsChanged() {

    }

    public String toString() {
        return "FluidContainer[context=%s, fluid=%s, amount=%d]".formatted(container, getFluidHolder().getFluid(), getFluidAmount());
    }

    public boolean preCondition() {
        return container.getCount() > 1;
    }

    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return this.fill(new FluidHolder(resource.getFluid(), (int) maxAmount), FluidAction.EXECUTE);
    }

    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return this.drain(new FluidHolder(resource.getFluid(), (int) maxAmount), FluidAction.EXECUTE).getAmount();
    }

    public boolean isResourceBlank() {
        return getFluidHolder().isEmpty();
    }

    public FluidVariant getResource() {
        return FluidVariant.of(getFluidHolder().getFluid());
    }

    public long getAmount() {
        return getFluidAmount();
    }

    public long getCapacity() {
        return getTankCapacity();
    }
}
