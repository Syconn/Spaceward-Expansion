package mod.syconn.swe.api.world.data.capability;

import mod.syconn.api.Constants;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public class APICapabilities {

    public static final class FluidHandler {
        public static final BlockCapability<IFluidHandlerInteractable, @Nullable Direction> BLOCK = BlockCapability.createSided(create("fluid_handler"), IFluidHandlerInteractable.class);
    }

    private static ResourceLocation create(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.ID, path);
    }
}
