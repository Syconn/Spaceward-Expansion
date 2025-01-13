package mod.syconn.swe.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import mod.syconn.swe.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModForge {
    public ExampleModForge(FMLJavaModLoadingContext context) {
        EventBuses.registerModEventBus(ExampleMod.MOD_ID, context.getModEventBus());

        // Run our common setup.
        ExampleMod.init();
    }
}
