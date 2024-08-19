package mod.syconn.swe.extra.platform.services;

import mod.syconn.swe.extra.core.FluidHolder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Optional;

public interface IFluidExtensions {

    int getTintColor(Fluid fluid);
    List<Component> getTooltip(Fluid fluid);
    Optional<TextureAtlasSprite> getStillTexture(Fluid fluid);
    Optional<TextureAtlasSprite> getFlowingTexture(Fluid fluid);

    default int getTintColor(FluidHolder fluidHolder) {
        return getTintColor(fluidHolder.getFluid());
    }

    default List<Component> getTooltip(FluidHolder fluidHolder) {
        return getTooltip(fluidHolder.getFluid());
    }

    default Optional<TextureAtlasSprite> getStillTexture(FluidHolder fluidHolder) {
        return getStillTexture(fluidHolder.getFluid());
    }

    default Optional<TextureAtlasSprite> getFlowingTexture(FluidHolder fluidHolder) {
        return getFlowingTexture(fluidHolder.getFluid());
    }
}
