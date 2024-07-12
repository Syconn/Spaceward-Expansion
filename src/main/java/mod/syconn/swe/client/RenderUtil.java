package mod.syconn.swe.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Map;

public class RenderUtil {

    public static int getFluidColor(FluidStack fluidStack) {
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluidType());
        int i = props.getTintColor(fluidStack);
        TextureAtlasSprite sprite = getSprite(fluidStack);
        int b = sprite.getPixelRGBA(0, 8, 8);
        int c = FastColor.ARGB32.color(FastColor.ARGB32.blue(b), FastColor.ARGB32.green(b), FastColor.ARGB32.red(b));
        if (i == -1) return c;
        return tintRGBA(c, i);
    }

    public static int tintRGBA(int color, int tintColor) {
        int r1 = FastColor.ARGB32.red(color);
        int g1 = FastColor.ARGB32.green(color);
        int b1 = FastColor.ARGB32.blue(color);
        int r2 = FastColor.ARGB32.red(tintColor);
        int g2 = FastColor.ARGB32.green(tintColor);
        int b2 = FastColor.ARGB32.blue(tintColor);
        int r = (r1 * (255 - 230) + r2 * 230) / 255;
        int g = (g1 * (255 - 230) + g2 * 230) / 255;
        int b = (b1 * (255 - 230) + b2 * 230) / 255;
        return FastColor.ARGB32.color(FastColor.ARGB32.alpha(color), r, g, b);
    }

    private static TextureAtlasSprite getSprite(ResourceLocation texture) { // TODO if works cache colors
        Map<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getTextures();
        return atlas.getOrDefault(texture, atlas.get(MissingTextureAtlasSprite.getLocation()));
    }

    private static TextureAtlasSprite getSprite(FluidStack fluidStack) { // TODO if works cache colors
        if (fluidStack.isEmpty()) return getSprite(MissingTextureAtlasSprite.getLocation());
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluidType());
        return getSprite(props.getStillTexture(fluidStack));
    }

    // TODO TF optimize
    public static NativeImage createFluidBlockTexture(Fluid fluid){
        TextureAtlasSprite sprite = getSprite(new FluidStack(fluid, 1));
        NativeImage result = new NativeImage(64, 64, false);
        for (int t = 1; t < 3; t++) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 15; y++) {
                    result.setPixelRGBA(16 * t + x , 1 + y, sprite.getPixelRGBA(0, x, y));
                }
            }
        }
        for (int t = 0; t < 4; t++) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 15; y++) {
                    result.setPixelRGBA( 16 * t + x, 16 + y, sprite.getPixelRGBA(0, x, y));
                }
            }
        }
        return result;
    }

    public static NativeImage createFluidGuiTexture(Fluid fluid){
        TextureAtlasSprite sprite = getSprite(new FluidStack(fluid, 1));
        NativeImage result = new NativeImage(80, 80, false);
        for (int w = 0; w < 5; w++) {
            for (int h = 0; h < 5; h++) {
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        result.setPixelRGBA(w * 16 + x, h * 16 + y, sprite.getPixelRGBA(0, x, y));
                    }
                }
            }
        }
        return result;
    }
}
