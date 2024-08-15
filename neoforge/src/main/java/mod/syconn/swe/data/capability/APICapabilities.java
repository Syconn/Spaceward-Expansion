package mod.syconn.swe.data.capability;

import mod.syconn.swe.Constants;
import mod.syconn.swe.extra.core.InteractionalFluidHandler;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public class APICapabilities {

    public static final class FluidHandler {
        public static final BlockCapability<InteractionalFluidHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(create("fluid_handler"), InteractionalFluidHandler.class);
    }

    private static ResourceLocation create(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
