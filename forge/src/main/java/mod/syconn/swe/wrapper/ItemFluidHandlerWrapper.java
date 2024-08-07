package mod.syconn.swe.wrapper;

import mod.syconn.swe.data.components.FluidComponent;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ItemFluidHandlerWrapper implements IFluidHandlerItem {
    protected final Supplier<DataComponentType<FluidComponent>> componentType;
    protected ItemStack container;
    protected int capacity;

    public ItemFluidHandlerWrapper(Supplier<DataComponentType<FluidComponent>> componentType, ItemStack container, int capacity) {
        this.componentType = componentType;
        this.container = container;
        this.capacity = capacity;
    }

    public ItemStack getContainer() {
        return container;
    }

    public FluidStack getFluid() {
        FluidComponent component = container.getOrDefault(componentType.get(), FluidComponent.EMPTY);
        return new FluidStack(component.fluid(), component.amount());
    }

    protected void setFluid(FluidStack fluid) {
        FluidComponent component = container.getOrDefault(componentType.get(), FluidComponent.EMPTY);
        container.set(componentType.get(), FluidComponent.of(fluid.getFluid(), fluid.getAmount(), component.capacity()));
    }
    
    public int getTanks() {
        return 1;
    }
    
    public FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    public int getTankCapacity(int tank) {
        return capacity;
    }
    
    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    public int fill(FluidStack resource, FluidAction doFill) {
        if (container.getCount() != 1 || resource.isEmpty() || !canFillFluidType(resource)) {
            return 0;
        }

        FluidStack contained = getFluid();
        if (contained.isEmpty()) {
            int fillAmount = Math.min(capacity, resource.getAmount());

            if (doFill.execute()) {
                setFluid(new FluidStack(contained.getFluid(), fillAmount, contained.getTag()));
            }

            return fillAmount;
        } else {
            if (FluidStack.areFluidStackTagsEqual(contained, resource)) {
                int fillAmount = Math.min(capacity - contained.getAmount(), resource.getAmount());

                if (doFill.execute() && fillAmount > 0) {
                    contained.grow(fillAmount);
                    setFluid(contained);
                }

                return fillAmount;
            }

            return 0;
        }
    }

    
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.isEmpty() || !FluidStack.areFluidStackTagsEqual(resource, getFluid())) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (container.getCount() != 1 || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        FluidStack contained = getFluid();
        if (contained.isEmpty() || !canDrainFluidType(contained)) {
            return FluidStack.EMPTY;
        }

        final int drainAmount = Math.min(contained.getAmount(), maxDrain);

        FluidStack drained = new FluidStack(contained.getFluid(), drainAmount, contained.getTag());

        if (action.execute()) {
            contained.shrink(drainAmount);
            if (contained.isEmpty()) {
                setContainerToEmpty();
            } else {
                setFluid(contained);
            }
        }

        return drained;
    }

    public boolean canFillFluidType(FluidStack fluid) {
        return true;
    }

    public boolean canDrainFluidType(FluidStack fluid) {
        return true;
    }

    protected void setContainerToEmpty() {
        container.remove(componentType.get());
    }

    public static class Consumable extends ItemFluidHandlerWrapper {
        public Consumable(Supplier<DataComponentType<FluidComponent>> componentType, ItemStack container, int capacity) {
            super(componentType, container, capacity);
        }

        protected void setContainerToEmpty() {
            super.setContainerToEmpty();
            container.shrink(1);
        }
    }

    public static class SwapEmpty extends ItemFluidHandlerWrapper {
        protected final ItemStack emptyContainer;

        public SwapEmpty(Supplier<DataComponentType<FluidComponent>> componentType, ItemStack container, ItemStack emptyContainer, int capacity) {
            super(componentType, container, capacity);
            this.emptyContainer = emptyContainer;
        }
        
        protected void setContainerToEmpty() {
            super.setContainerToEmpty();
            container = emptyContainer;
        }
    }
}