package mod.syconn.swe.forge;

import dev.architectury.platform.forge.EventBuses;
import mod.syconn.swe.Constants;
import mod.syconn.swe.SpaceMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD)
public final class SpaceModForge {

    public SpaceModForge() {
        EventBuses.registerModEventBus(Constants.MOD, FMLJavaModLoadingContext.get().getModEventBus());
        SpaceMod.init();
    }
}
