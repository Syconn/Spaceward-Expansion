package mod.syconn.swe.item.extras;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface ItemFluidHandler {

    FluidStack getFluid(ItemStack stack);
    void setFluid(ItemStack item, FluidStack fluid);
    ItemStack create(FluidStack stack);
    ItemStack createEmpty();
    int getColor(ItemStack stack);
    int getCapacity(ItemStack stack);
    void setAmount(ItemStack stack, int v, Fluid fluid);
    default int getSpace(ItemStack stack) {
        return getCapacity(stack) - getFluid(stack).getAmount();
    }
}
