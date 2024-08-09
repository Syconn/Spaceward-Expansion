package mod.syconn.swe.common.data.capabilities;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;

import java.util.function.Supplier;

public class ItemFluidWrapper extends FluidHandlerItemStack {

    public ItemFluidWrapper(Supplier<DataComponentType<SimpleFluidContent>> componentType, ItemStack container, int capacity) {
        super(componentType, container, capacity);
    }

    protected void setContainerToEmpty() {
        setFluid(FluidStack.EMPTY);
    }
}
