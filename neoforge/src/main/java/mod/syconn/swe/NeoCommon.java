package mod.syconn.swe;

import mod.syconn.swe.wrapper.ItemFluidHandlerWrapper;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static mod.syconn.swe.init.ComponentRegister.FLUID_COMPONENT;
import static mod.syconn.swe.init.ItemRegister.AUTO_REFILL_CANISTER;
import static mod.syconn.swe.init.ItemRegister.CANISTER;

public class NeoCommon {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new ItemFluidHandlerWrapper(FLUID_COMPONENT, stack, 8000), CANISTER.get(), AUTO_REFILL_CANISTER.get());
    }
}
