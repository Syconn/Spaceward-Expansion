package mod.syconn.swe.wrappers;

import mod.syconn.swe.data.components.FluidComponent;
import mod.syconn.swe.platform.services.ISingleFluidHandler;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class ComponentFluidWrapper implements SingleSlotStorage<FluidVariant> {
    protected final DataComponentType<FluidComponent> componentType;
    protected final ItemStack container;
    protected int capacity;

    public ComponentFluidWrapper(Supplier<DataComponentType<FluidComponent>> componentType, ContainerItemContext context, int capacity) {
        this.componentType = componentType.get();
        this.container = context.getItemVariant().toStack();
        this.capacity = capacity;
    }

    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (container.getCount() != 1 || maxAmount <= 0) {
            return 0;
        }

        ISingleFluidHandler.FluidHolder contained = getFluid();
        if (contained.fluid().isSame(Fluids.EMPTY) || getFluid().is(resource.getFluid())) {
            return 0;
        }

        int drainAmount = (int) Math.min(contained.amount(), maxAmount);
        setFluid(contained.shrink(drainAmount));
        return drainAmount;
    }

    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (container.getCount() != 1 || resource.getFluid().isSame(Fluids.EMPTY)) {
            return 0;
        }

        ISingleFluidHandler.FluidHolder contained = getFluid();
        if (contained.fluid().isSame(Fluids.EMPTY)) {
            int fillAmount = (int) Math.min(capacity, maxAmount);
            setFluid(new ISingleFluidHandler.FluidHolder(resource.getFluid(), fillAmount));
            return fillAmount;
        } else {
            if (resource.getFluid().isSame(contained.fluid())) {
                int fillAmount = (int) Math.min(capacity - contained.amount(), maxAmount);
                if (fillAmount > 0) setFluid(new ISingleFluidHandler.FluidHolder(getFluid().fluid(), contained.amount() + fillAmount));
                return fillAmount;
            }

            return 0;
        }
    }

    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    public FluidVariant getResource() {
        FluidComponent component = container.getOrDefault(componentType, FluidComponent.EMPTY);
        return FluidVariant.of(component.fluid());
    }

    public ISingleFluidHandler.FluidHolder getFluid() {
        return new ISingleFluidHandler.FluidHolder(getResource().getFluid(), (int) getAmount());
    }

    protected void setFluid(ISingleFluidHandler.FluidHolder fluid) {
        FluidComponent component = container.getOrDefault(componentType, FluidComponent.EMPTY);
        container.set(componentType, FluidComponent.of(fluid.fluid(), fluid.amount(), component.capacity()));
    }

    public long getAmount() {
        FluidComponent component = container.getOrDefault(componentType, FluidComponent.EMPTY);
        return component.amount();
    }

    public long getCapacity() {
        return capacity;
    }

    public String toString() {
        return "FluidContainer[context=%s, fluid=%s, amount=%d]".formatted(container, getResource(), getAmount());
    }
}
