package mod.syconn.swe;

import mod.syconn.swe.fluids.ForgeOxygenFluid;
import mod.syconn.swe.helper.FluidTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

import java.util.function.Supplier;

import static mod.syconn.swe.blocks.fluids.OxygenFlowingFluid.*;

public class ForgeRegistration {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.FLUID_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Constants.MOD_ID);

    public static Supplier<FluidTypes> O2_FLUID_TYPE = FLUID_TYPES.register("oxygen", () -> new FluidTypes(O2_STILL_RL, O2_FLOWING_RL, O2_OVERLAY_RL, -1, new Vector3f(68f / 255f, 149f / 255f, 168f / 255f), FluidType.Properties.create().descriptionId("swe.fluid.o2").canSwim(true).canExtinguish(false).canDrown(false)
            .pathType(PathType.WATER).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).lightLevel(1).density(15).viscosity(5)));

    public static final Supplier<Source> O2 = FLUIDS.register("oxygen", ForgeOxygenFluid.Source::new);
    public static final Supplier<Flowing> O2_FLOWING = FLUIDS.register("oxygen_flowing", ForgeOxygenFluid.Flowing::new);

    public static void init() {}
}
