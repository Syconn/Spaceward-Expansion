package mod.syconn.swe.util.core;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Predicate;

public class FluidTank implements FluidHandler {

    private final int capacity;
    private Predicate<FluidHolder> validator;
    private FluidHolder fluid = FluidHolder.EMPTY;

    public FluidTank(int capacity, Predicate<FluidHolder> predicate) {
        this.capacity = capacity;
        this.validator = predicate;
    }

    public FluidTank(int capacity) {
        this(capacity, entry -> true);
    }

    public FluidHolder getFluidHolder() {
        return fluid;
    }

    public int getTankCapacity() {
        return capacity;
    }

    public void setValidator(Predicate<FluidHolder> validator) {
        this.validator = validator;
    }

    public boolean isFluidValid(FluidHolder holder) {
        return validator.test(holder);
    }

    public void setFluid(FluidHolder fluidHolder) {
        this.onContentsChanged();
        this.fluid = fluidHolder;
    }

    public void onContentsChanged() {}

    public CompoundTag writeNBT(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        if (!fluid.isEmpty()) tag.put("fluid", fluid.save(lookupProvider));
        return tag;
    }

    public FluidHandler readNBT(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        fluid = FluidHolder.parseOptional(lookupProvider, tag.getCompound("fluid"));
        return this;
    }
}
