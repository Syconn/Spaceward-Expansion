package mod.syconn.swe.common.items;

import dev.architectury.fluid.FluidStack;
import mod.syconn.swe.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

public interface FluidHolderItem {
    String FLUID = "fluidstack";
    String MAX = "max";
    String ELEMENT = Constants.MOD + ":properties";
    FluidStack EMPTY = FluidStack.empty();

    long defaultMax();

    default boolean isEmpty(ItemStack stack) {
        CompoundTag compoundTag = stack.getTagElement(ELEMENT);
        return compoundTag != null && getFluidStack(stack).getFluid().isSame(Fluids.EMPTY);
    }

    default FluidStack getFluidStack(ItemStack stack) {
        CompoundTag compoundTag = stack.getTagElement(ELEMENT);
        return compoundTag != null && compoundTag.contains(FLUID) ? FluidStack.read(compoundTag.getCompound(FLUID)) : EMPTY;
    }

    default long getMax(ItemStack stack) {
        CompoundTag compoundTag = stack.getTagElement(ELEMENT);
        return compoundTag != null && compoundTag.contains(MAX) ? compoundTag.getLong(MAX) : defaultMax();
    }

    default void setFluidStack(ItemStack stack, FluidStack fluidStack) {
        stack.getOrCreateTagElement(ELEMENT).put(FLUID, fluidStack.getOrCreateTag());
        if (!stack.getOrCreateTagElement(ELEMENT).contains(MAX)) stack.getOrCreateTagElement(ELEMENT).putLong(MAX, defaultMax());
    }

    default void emptyHolder(ItemStack stack) {
        setFluidStack(stack, EMPTY);
    }

    default boolean fill(ItemStack stack, FluidStack fill, boolean simulate) {
        if (simulate) return (fill.isFluidEqual(getFluidStack(stack)) || getFluidStack(stack).getFluid().isSame(Fluids.EMPTY)) && fill.getAmount() <= getFluidStack(stack).getAmount();
        return fill(stack, fill.getAmount(), false);
    }

    default boolean fill(ItemStack stack, long fill, boolean simulate) {
        if (simulate) return fill <= getFluidStack(stack).getAmount();
        if (fill <= getFluidStack(stack).getAmount()) {
            setFluidStack(stack, getFluidStack(stack).copyWithAmount(fill + getFluidStack(stack).getAmount()));
            return true;
        }
        return false;
    }

    default boolean drain(ItemStack stack, FluidStack drain, boolean simulate) {
        if (simulate) return drain.isFluidEqual(getFluidStack(stack)) && drain.getAmount() <= getFluidStack(stack).getAmount();
        return drain(stack, drain.getAmount(), false);
    }

    default boolean drain(ItemStack stack, long drain, boolean simulate) {
        if (simulate) return drain <= getFluidStack(stack).getAmount();
        if (drain <= getFluidStack(stack).getAmount()) {
            setFluidStack(stack, getFluidStack(stack).copyWithAmount(getFluidStack(stack).getAmount() - drain));
            return true;
        }
        return false;
    }

    static ItemStack create(FluidStack fluidStack, Item item) {
        ItemStack itemStack = new ItemStack(item);
        if (item instanceof FluidHolderItem holder) holder.setFluidStack(itemStack, fluidStack);
        return itemStack;
    }

    static ItemStack createEmpty(Item item) {
        ItemStack itemStack = new ItemStack(item);
        if (item instanceof FluidHolderItem holder) holder.emptyHolder(itemStack);
        return itemStack;
    }
}
