package mod.syconn.swe;

import mod.syconn.swe.blocks.fluids.OxygenFlowingFluid;
import mod.syconn.swe.helper.FluidTypes;
import mod.syconn.swe.helper.NeoOxygenFluid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

import java.util.function.Supplier;

import static mod.syconn.swe.blocks.fluids.OxygenFlowingFluid.*;

public class NeoRegistration {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Constants.MOD_ID);

    public static Supplier<FluidTypes> O2_FLUID_TYPE = FLUID_TYPES.register("oxygen", () -> new FluidTypes(O2_STILL_RL, O2_FLOWING_RL, O2_OVERLAY_RL, -1, new Vector3f(68f / 255f, 149f / 255f, 168f / 255f), FluidType.Properties.create().descriptionId("swe.fluid.o2").canSwim(true).canExtinguish(false).canDrown(false)
            .pathType(PathType.WATER).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).lightLevel(1).density(15).viscosity(5)));

    public static final Supplier<OxygenFlowingFluid.Source> O2 = FLUIDS.register("oxygen", NeoOxygenFluid.Source::new);
    public static final Supplier<OxygenFlowingFluid.Flowing> O2_FLOWING = FLUIDS.register("oxygen_flowing", NeoOxygenFluid.Flowing::new);

    public static void init() {}
}
