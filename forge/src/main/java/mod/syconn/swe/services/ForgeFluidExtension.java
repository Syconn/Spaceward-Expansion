package mod.syconn.swe.services;

import mod.syconn.swe.platform.services.IFluidExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public class ForgeFluidExtension implements IFluidExtensions {

    public int getTintColor(Fluid fluid) {
        return get(fluid).getTintColor();
    }

    public List<Component> getTooltip(Fluid fluid) {
        return List.of(new FluidStack(fluid, 1).getDisplayName());
    }

    public Optional<TextureAtlasSprite> getStillTexture(Fluid fluid) {
        return Optional.of(get(fluid).getStillTexture()).map(f -> Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(f)).filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }

    public Optional<TextureAtlasSprite> getFlowingTexture(Fluid fluid) {
        return Optional.of(get(fluid).getFlowingTexture()).map(f -> Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(f)).filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }

    private IClientFluidTypeExtensions get(Fluid fluid) {
        return IClientFluidTypeExtensions.of(fluid);
    }
}
