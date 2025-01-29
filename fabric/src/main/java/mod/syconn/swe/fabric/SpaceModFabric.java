package mod.syconn.swe.fabric;

import mod.syconn.swe.SpaceMod;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;

public final class SpaceModFabric implements ModInitializer {

    public void onInitialize() {
        FluidStorage.GENERAL_COMBINED_PROVIDER.register(context -> {

        });

        SpaceMod.init();
    }
}
