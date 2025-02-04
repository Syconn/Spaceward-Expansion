package mod.syconn.swe.registry;

import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.registry.registries.DeferredRegister;
import mod.syconn.swe.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

import static mod.syconn.swe.Constants.MOD;

public class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(MOD, Registries.FLUID);

    public static final ArchitecturyFluidAttributes OXYGEN_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(() -> ModFluids.O2_FLOWING, () -> ModFluids.O2).block(ModBlocks.O2_FLUID_BLOCK)
            .color(-1).overlayTexture(Constants.withId("block/o2_overlay.png")).sourceTexture(Constants.withId("block/o2_still")).flowingTexture(Constants.withId("block/o2_flowing"))
            .fillSound(SoundEvents.BUCKET_FILL).emptySound(SoundEvents.BUCKET_EMPTY).density(15).viscosity(5);

    public static final Supplier<FlowingFluid> O2 = FLUIDS.register("oxygen", () -> new ArchitecturyFlowingFluid.Source(OXYGEN_ATTRIBUTES));
    public static final Supplier<FlowingFluid> O2_FLOWING = FLUIDS.register("oxygen_flowing", () -> new ArchitecturyFlowingFluid.Flowing(OXYGEN_ATTRIBUTES));
}
