package mod.syconn.swe.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Map;

public class RenderUtil {

    public static int getFluidColor(FluidStack fluidStack) {
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluidType());
        int i = props.getTintColor(fluidStack);
        TextureAtlasSprite sprite = getSprite(props.getFlowingTexture(fluidStack));
        int b = sprite.getPixelRGBA(1, 8, 8);
        System.out.println("R: " + FastColor.ARGB32.red(b) + " G: " + FastColor.ARGB32.green(b) + " B: " + FastColor.ARGB32.blue(b));
        return FastColor.ARGB32.color(tint(FastColor.ARGB32.blue(b), FastColor.ARGB32.red(i)), tint(FastColor.ARGB32.green(b), FastColor.ARGB32.green(i)),
                tint(FastColor.ARGB32.red(b), FastColor.ARGB32.blue(i)));
    }

    public static int tint(int c, int t) {
        return (int) (c * t * 0.1);
    }

    private static TextureAtlasSprite getSprite(ResourceLocation texture) { // TODO if works cache this
        Map<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getTextures();
        return atlas.getOrDefault(texture, atlas.get(MissingTextureAtlasSprite.getLocation()));
    }
}
