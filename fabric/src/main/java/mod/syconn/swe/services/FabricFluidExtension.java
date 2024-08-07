package mod.syconn.swe.services;

import mod.syconn.swe.platform.services.IFluidExtensions;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Optional;

public class FabricFluidExtension implements IFluidExtensions {

    public int getTintColor(Fluid fluid) {
        return FluidVariantRendering.getColor(FluidVariant.of(fluid));
    }

    public List<Component> getTooltip(Fluid fluid) {
        return FluidVariantRendering.getTooltip(FluidVariant.of(fluid));
    }

    public Optional<TextureAtlasSprite> getStillTexture(Fluid fluid) {
        return Optional.ofNullable(FluidVariantRendering.getSprites(FluidVariant.of(fluid))[0]);
    }

    public Optional<TextureAtlasSprite> getFlowingTexture(Fluid fluid) {
        return Optional.ofNullable(FluidVariantRendering.getSprites(FluidVariant.of(fluid))[2]);
    }
}
