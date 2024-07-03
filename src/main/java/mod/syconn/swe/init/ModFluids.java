package mod.syconn.swe.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;
import mod.syconn.swe.Main;
import mod.syconn.swe.block.fluid.BaseFluidType;

public class ModFluids {

    private static final ResourceLocation O2_STILL_RL = new ResourceLocation(Main.MODID, "block/o_still");
    private static final ResourceLocation O2_FLOWING_RL = new ResourceLocation(Main.MODID,"block/o2_flowing");
    private static final ResourceLocation O2_OVERLAY_RL = new ResourceLocation(Main.MODID, "block/o2_overlay.png");

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Main.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.Keys.FLUIDS, Main.MODID);

    public static RegistryObject<FluidType> O2_FLUID_TYPE = FLUID_TYPES.register("o2_fluid", () -> new BaseFluidType(O2_STILL_RL, O2_FLOWING_RL, O2_OVERLAY_RL, -1, new Vector3f(68f / 255f, 149f / 255f, 168f / 255f), FluidType.Properties.create().descriptionId("swe.fluid.o2").canSwim(true).canExtinguish(false).canDrown(false)
            .pathType(BlockPathTypes.WATER).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).lightLevel(1)
            .density(15).viscosity(5)
    ));

    public static final RegistryObject<FlowingFluid> SOURCE_O2_FLUID = FLUIDS.register("source_o2_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.O2_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_O2_FLUID = FLUIDS.register("flowing_o2_fluid", () -> new ForgeFlowingFluid.Flowing(ModFluids.O2_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties O2_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(O2_FLUID_TYPE, SOURCE_O2_FLUID, FLOWING_O2_FLUID)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModInit.O2_FLUID_BLOCK).bucket(ModInit.O2_BUCKET);
}