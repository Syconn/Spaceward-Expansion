package mod.syconn.swe.core;

import mod.syconn.swe.common.blocks.fluids.OxygenFlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class FluidRegister {

    public static final Supplier<OxygenFlowingFluid.Source> O2 = register("oxygen", OxygenFlowingFluid.Source.class);
    public static final Supplier<OxygenFlowingFluid.Flowing> O2_FLOWING = register("oxygen_flowing", OxygenFlowingFluid.Flowing.class);

    private static <T extends Fluid> Supplier<T> register(String id, Class<T> fluid) {
        return Services.REGISTRAR.registerFluid(id, fluid);
    }
}
