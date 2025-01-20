package mod.syconn.swe.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExampleMod.MOD_ID)
public final class SpaceModForge {

    public SpaceModForge(FMLJavaModLoadingContext context) {
        EventBuses.registerModEventBus(ExampleMod.MOD_ID, context.getModEventBus());
        ExampleMod.init();
    }
}
