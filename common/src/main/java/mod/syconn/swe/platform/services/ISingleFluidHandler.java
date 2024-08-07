package mod.syconn.swe.platform.services;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public interface ISingleFluidHandler {

    ISingleFluidHandler get(ItemStack stack);
    FluidHolder getFluidInTank();
    int getTankCapacity();
    int fill(FluidHolder resource, FluidAction action);
    FluidHolder drain(FluidHolder resource, FluidAction action);
    FluidHolder drain(int drain, FluidAction action);

    record FluidHolder(Fluid fluid, int amount) {
        public boolean is(Fluid fluid) {
            return this.fluid.isSame(fluid);
        }

        public FluidHolder shrink(int drainAmount) {
            return new FluidHolder(fluid, Math.min(0, amount - drainAmount));
        }
    }
    enum FluidAction {
        EXECUTE,
        SIMULATE
    }
}
