package mod.syconn.swe.platform.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public interface IFluidExtensions { // TODO FABRIC FluidVariantRendering

    int getTintColor(Fluid fluid);
    ResourceLocation getStillTexture(Fluid fluid);
    ResourceLocation getFlowingTexture(Fluid fluid);
}
