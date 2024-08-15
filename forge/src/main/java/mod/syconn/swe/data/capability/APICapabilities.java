package mod.syconn.swe.data.capability;

import mod.syconn.swe.extra.core.InteractionalFluidHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class APICapabilities {

    public static final Capability<InteractionalFluidHandler> INTERACTIONAL_HANDLER = CapabilityManager.get(new CapabilityToken<>() {});
}
