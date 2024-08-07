package mod.syconn.swe.platform.services;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;

public interface ISingleFluidHandler {

    ISingleFluidHandler get(Player player, InteractionHand hand);
    FluidHolder getFluidInTank();
    int getTankCapacity();
    int fill(FluidHolder resource, FluidAction action);
    FluidHolder drain(FluidHolder resource, FluidAction action);
    FluidHolder drain(int drain, FluidAction action);

    record FluidHolder(Fluid fluid, int amount) {
        public boolean is(Fluid fluid) {
            return this.fluid.isSame(fluid);
        }
    }
    enum FluidAction {
        EXECUTE,
        SIMULATE
    }
}
