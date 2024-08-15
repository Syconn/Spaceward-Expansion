package mod.syconn.swe.extra.core;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluids;

public interface FluidHandler {

    FluidHolder getFluid();
    int getCapacity();
    void setFluid(FluidHolder fluidHolder);
    boolean isFluidValid(FluidHolder holder);
    void onContentsChanged();

    default CompoundTag writeNBT(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        return new CompoundTag();
    }

    default FluidHandler readNBT(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        return this;
    }

    default boolean preCondition() {
        return false;
    }

    default int fill(FluidHolder resource, FluidAction doFill) {
        if (preCondition() || isFluidValid(resource) || resource.is(Fluids.EMPTY)) return 0;
        FluidHolder contained = getFluid();
        if (contained.is(Fluids.EMPTY)) {
            int fillAmount = Math.min(getCapacity(), resource.getAmount());
            if (doFill == FluidAction.EXECUTE) setFluid(resource.copyWith(fillAmount));
            return fillAmount;
        } else {
            if (contained.is(resource)) {
                int fillAmount = Math.min(getCapacity() - contained.getAmount(), resource.getAmount());
                if (doFill == FluidAction.EXECUTE && fillAmount > 0) setFluid(contained.fill(fillAmount));
                return fillAmount;
            }
            return 0;
        }
    }

    default FluidHolder drain(FluidHolder resource, FluidAction action) {
        if (preCondition() || resource.isEmpty() || !resource.is(getFluid())) return FluidHolder.EMPTY;
        return drain(resource.getAmount(), action);
    }

    default FluidHolder drain(int maxDrain, FluidAction action) {
        if (preCondition() || maxDrain <= 0) return FluidHolder.EMPTY;
        FluidHolder contained = getFluid();
        if (contained.is(Fluids.EMPTY)) return FluidHolder.EMPTY;
        final int drainAmount = Math.min(contained.getAmount(), maxDrain);
        if (action == FluidAction.EXECUTE) setFluid(contained.shrink(drainAmount));
        return contained.copyWith(drainAmount);
    }
}
