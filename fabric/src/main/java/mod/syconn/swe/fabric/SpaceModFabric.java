package mod.syconn.swe.fabric;

import mod.syconn.swe.SpaceMod;
import net.fabricmc.api.ModInitializer;

public final class SpaceModFabric implements ModInitializer {

    public void onInitialize() {
        SpaceMod.init();
    }
}
