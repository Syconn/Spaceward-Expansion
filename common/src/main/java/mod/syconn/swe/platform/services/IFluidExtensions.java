package mod.syconn.swe.platform.services;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Optional;

public interface IFluidExtensions { // TODO FABRIC

    int getTintColor(Fluid fluid);
    List<Component> getTooltip(Fluid fluid);
    Optional<TextureAtlasSprite> getStillTexture(Fluid fluid);
    Optional<TextureAtlasSprite> getFlowingTexture(Fluid fluid);
}
