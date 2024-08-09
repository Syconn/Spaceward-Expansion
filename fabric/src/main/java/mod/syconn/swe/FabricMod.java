package mod.syconn.swe;

import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.events.LivingEntityEvents;
import mod.syconn.swe.init.ComponentRegister;
import mod.syconn.swe.items.Canister;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.util.Events;
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
        // TODO FIGURE OUT CONFIG
        LivingEntityEvents.FALL_EVENT.register(((livingEntity, distance, damageMultiplier, cancelled) -> CommonHandler.livingFallEvent(new Events.LivingFallEvent(livingEntity, distance, damageMultiplier, cancelled))));

        SpaceMod.init();
        Network.S2CPayloads();
    }
}
