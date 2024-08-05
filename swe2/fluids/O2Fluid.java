package mod.syconn.swe2.fluids;

import mod.syconn.swe2.Registration;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class O2Fluid extends BaseFlowingFluid {

    public static final BaseFlowingFluid.Properties PROPERTIES = new BaseFlowingFluid.Properties(Registration.O2_FLUID_TYPE, Registration.O2, Registration.O2_FLOWING)
            .block(Registration.O2_FLUID_BLOCK).bucket(Registration.O2_BUCKET).slopeFindDistance(3);

    public O2Fluid(Properties properties) {
        super(properties);
    }
}