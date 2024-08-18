package mod.syconn.swe.fluids;

import mod.syconn.swe.NeoRegistration;
import mod.syconn.swe.blocks.fluids.OxygenFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;

public class NeoOxygenFluid {

    public static class Flowing extends OxygenFlowingFluid.Flowing {

        public FluidType getFluidType() {
            return NeoRegistration.O2_FLUID_TYPE.get();
        }
    }

    public static class Source extends OxygenFlowingFluid.Source {

        public FluidType getFluidType() {
            return NeoRegistration.O2_FLUID_TYPE.get();
        }
    }
}
