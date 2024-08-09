package mod.syconn.swe.init;

import mod.syconn.swe.fluids.OxygenFlowingFluid;
import mod.syconn.swe.platform.Services;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class FluidRegister {

    public static final Supplier<FlowingFluid> O2 = register("oxygen", OxygenFlowingFluid.Source::new);
    public static final Supplier<FlowingFluid> O2_FLOWING = register("oxygen_flowing", OxygenFlowingFluid.Flowing::new);

    private static <T extends Fluid> Supplier<T> register(String id, Supplier<T> fluidSupplier) {
        return Services.REGISTRAR.registerFluid(id, fluidSupplier);
    }

    public static void init() {
        Services.REGISTRAR.registerFluidType("o2", OxygenFlowingFluid.O2_STILL_RL, OxygenFlowingFluid.O2_FLOWING_RL, OxygenFlowingFluid.O2_OVERLAY_RL, -1, new Vector3f(68f / 255f, 149f / 255f, 168f / 255f),
                "swe.fluid.o2", true, false, false, PathType.WATER, 1, 15, 5, SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH);
    }
}
