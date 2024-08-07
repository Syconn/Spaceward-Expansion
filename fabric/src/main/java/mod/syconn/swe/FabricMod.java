package mod.syconn.swe;

import mod.syconn.swe.init.ComponentRegister;
import mod.syconn.swe.items.Canister;
import mod.syconn.swe.wrappers.ComponentFluidWrapper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;

public class FabricMod implements ModInitializer {
    
    public void onInitialize() {
        FluidStorage.GENERAL_COMBINED_PROVIDER.register(context -> {
            if (context.getItemVariant().getItem() instanceof Canister) {
                return new ComponentFluidWrapper(ComponentRegister.FLUID_COMPONENT, context, 8000);
            }
            return null;
        });

        SpaceMod.init();
    }
}
