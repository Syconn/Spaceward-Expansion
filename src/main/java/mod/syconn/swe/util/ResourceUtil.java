package mod.syconn.swe.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Optional;

public class ResourceUtil {

    public static Optional<NativeImage> getMainImage(TextureAtlasSprite sprite) {
        SpriteContents contents = sprite.contents();
        NativeImage[] frames = contents.byMipLevel;
        if (frames.length == 0) {
            return Optional.empty();
        }
        NativeImage frame = frames[0];
        return Optional.ofNullable(frame);
    }


    // FROM JEI FLUID HELPER CLASS
    public static Optional<TextureAtlasSprite> getStillFluidSprite(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation fluidStill = renderProperties.getStillTexture(fluidStack);
        return Optional.ofNullable(fluidStill)
                .map(f -> Minecraft.getInstance()
                        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(f)
                )
                .filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }


    // TODO TF optimize
    public static NativeImage createFluidBlockTexture(Fluid fluid){
        TextureAtlasSprite sprite = getStillFluidSprite(new FluidStack(fluid, 1)).get();
        NativeImage input = sprite.contents().getOriginalImage();
        NativeImage result = new NativeImage(64, 64, false);
        for (int t = 1; t < 3; t++) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 15; y++) {
                    result.setPixelRGBA(16 * t + x , 1 + y, input.getPixelRGBA(x, y));
                }
            }
        }
        for (int t = 0; t < 4; t++) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 15; y++) {
                    result.setPixelRGBA( 16 * t + x, 16 + y, input.getPixelRGBA(x, y));
                }
            }
        }
        return result;
    }

    public static NativeImage createFluidGuiTexture(Fluid fluid){
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(IClientFluidTypeExtensions.of(fluid).getStillTexture());
        NativeImage input = sprite.contents().getOriginalImage();
        NativeImage result = new NativeImage(80, 80, false);
        for (int w = 0; w < 5; w++) {
            for (int h = 0; h < 5; h++) {
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        result.setPixelRGBA(w * 16 + x, h * 16 + y, input.getPixelRGBA(x, y));
                    }
                }
            }
        }
        return result;
    }
}
