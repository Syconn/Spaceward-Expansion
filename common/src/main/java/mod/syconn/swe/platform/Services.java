package mod.syconn.swe.platform;

import mod.syconn.swe.Constants;
import mod.syconn.swe.platform.services.*;

import java.util.ServiceLoader;

public class Services {

    public static final IRegistrar REGISTRAR = load(IRegistrar.class);
    public static final ISingleFluidHandler FLUID_HANDLER = load(ISingleFluidHandler.class);
    public static final IFluidExtensions FLUID_EXTENSIONS = load(IFluidExtensions.class);
    public static final INetwork NETWORK = load(INetwork.class);
    public static final IAttachedData ATTACHED_DATA = load(IAttachedData.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
