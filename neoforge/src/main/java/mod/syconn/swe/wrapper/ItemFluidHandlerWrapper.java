package mod.syconn.swe.wrapper;

import mod.syconn.swe.extra.data.components.FluidHolderComponent;
import mod.syconn.swe.init.ComponentRegister;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class ItemFluidHandlerWrapper implements IFluidHandlerItem {
    
    protected ItemStack container;
    protected int capacity;

    public ItemFluidHandlerWrapper(ItemStack container, int capacity) {
        this.container = container;
        this.capacity = capacity;
    }

    public ItemStack getContainer() {
        return container;
    }

    public FluidStack getFluid() {
        FluidHolderComponent component = container.getOrDefault(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.EMPTY);
        return new FluidStack(component.fluidHolder().getFluid(), component.fluidHolder().getAmount());
    }

    protected void setFluid(FluidStack fluid) {
        FluidHolderComponent component = container.getOrDefault(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.EMPTY);
        container.set(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.of(fluid.getFluid(), fluid.getAmount(), component.capacity()));
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
                setFluid(resource.copyWithAmount(fillAmount));
            }

            return fillAmount;
        } else {
            if (FluidStack.isSameFluidSameComponents(contained, resource)) {
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
        if (container.getCount() != 1 || resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, getFluid())) {
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

        FluidStack drained = contained.copyWithAmount(drainAmount);

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
        container.set(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.EMPTY);
    }
}