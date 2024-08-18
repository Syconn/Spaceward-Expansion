package mod.syconn.swe.fluids;

import mod.syconn.swe.ForgeRegistration;
import mod.syconn.swe.blocks.fluids.OxygenFlowingFluid;
import net.minecraftforge.fluids.FluidType;

public class ForgeOxygenFluid {

    public static class Flowing extends OxygenFlowingFluid.Flowing {

        public FluidType getFluidType() {
            return ForgeRegistration.O2_FLUID_TYPE.get();
        }
    }

    public static class Source extends OxygenFlowingFluid.Source {

        public FluidType getFluidType() {
            return ForgeRegistration.O2_FLUID_TYPE.get();
        }
    }
}
