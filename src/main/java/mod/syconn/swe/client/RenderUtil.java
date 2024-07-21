package mod.syconn.swe.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Map;

public class RenderUtil {

    public static int getFluidColor(FluidStack fluidStack) {
        if (fluidStack.isEmpty()) return -1;
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluidType());
        int i = props.getTintColor(fluidStack);
        TextureAtlasSprite sprite = getSprite(fluidStack);
        int b = sprite.getPixelRGBA(0, 8, 8);
        int c = FastColor.ARGB32.color(FastColor.ARGB32.blue(b), FastColor.ARGB32.green(b), FastColor.ARGB32.red(b));
        if (i == -1) return c;
        return tintRGBA(c, i);
    }

    private static int tintRGBA(int color, int tintColor) {
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

    public static TextureAtlasSprite getSprite(FluidStack fluidStack) { // TODO if works cache colors
        if (fluidStack.isEmpty()) return getSprite(MissingTextureAtlasSprite.getLocation());
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluidType());
        return getSprite(props.getStillTexture(fluidStack));
    }

    //TODO Could optimize with positional data
    public static void renderLiquid(PoseStack pPoseStack, MultiBufferSource pBufferSource, Fluid fluid, float minScale, float maxScale, float height) {
        if (!fluid.isSame(Fluids.EMPTY)) {
            IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(fluid);
            ResourceLocation fluidStill = extension.getStillTexture();
            int tint = extension.getTintColor(new FluidStack(fluid, 1));
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
            VertexConsumer builder = pBufferSource.getBuffer(RenderType.translucent());

            pPoseStack.pushPose();
            // Top Face
            if (height < 0.99f) {
                add(builder, pPoseStack, minScale, height, maxScale, sprite.getU0(), sprite.getV1(), tint);
                add(builder, pPoseStack, maxScale, height, maxScale, sprite.getU1(), sprite.getV1(), tint);
                add(builder, pPoseStack, maxScale, height, minScale, sprite.getU1(), sprite.getV0(), tint);
                add(builder, pPoseStack, minScale, height, minScale, sprite.getU0(), sprite.getV0(), tint);
            }

            // Front Faces [NORTH - SOUTH]
            add(builder, pPoseStack, maxScale, height, maxScale, sprite.getU0(), sprite.getV0(), tint);
            add(builder, pPoseStack, minScale, height, maxScale, sprite.getU1(), sprite.getV0(), tint);
            add(builder, pPoseStack, minScale, minScale, maxScale, sprite.getU1(), sprite.getV1(), tint);
            add(builder, pPoseStack, maxScale, minScale, maxScale, sprite.getU0(), sprite.getV1(), tint);

            add(builder, pPoseStack, maxScale, minScale, minScale, sprite.getU0(), sprite.getV1(), tint);
            add(builder, pPoseStack, minScale, minScale, minScale, sprite.getU1(), sprite.getV1(), tint);
            add(builder, pPoseStack, minScale, height, minScale, sprite.getU1(), sprite.getV0(), tint);
            add(builder, pPoseStack, maxScale, height, minScale, sprite.getU0(), sprite.getV0(), tint);

            pPoseStack.mulPose(Axis.YP.rotationDegrees(90)); // TODO FIX WITH BETTER VECTORS
            pPoseStack.translate(-1f, 0, 0);

            // Front Faces [EAST - WEST]
            add(builder, pPoseStack, maxScale, height, maxScale, sprite.getU0(), sprite.getV0(), tint);
            add(builder, pPoseStack, minScale, height, maxScale, sprite.getU1(), sprite.getV0(), tint);
            add(builder, pPoseStack, minScale, minScale, maxScale, sprite.getU1(), sprite.getV1(), tint);
            add(builder, pPoseStack, maxScale, minScale, maxScale, sprite.getU0(), sprite.getV1(), tint);

            add(builder, pPoseStack, maxScale, minScale, minScale, sprite.getU0(), sprite.getV1(), tint);
            add(builder, pPoseStack, minScale, minScale, minScale, sprite.getU1(), sprite.getV1(), tint);
            add(builder, pPoseStack, minScale, height, minScale, sprite.getU1(), sprite.getV0(), tint);
            add(builder, pPoseStack, maxScale, height, minScale, sprite.getU0(), sprite.getV0(), tint);
//
//                        // Bottom Face of Top
//            add(builder, pPoseStack, 1, height, 1, sprite.getU0(), sprite.getV1(), tint);
//            add(builder, pPoseStack, 0, height, 1, sprite.getU1(), sprite.getV1(), tint);
//            add(builder, pPoseStack, 0, height, 0, sprite.getU1(), sprite.getV0(), tint);
//            add(builder, pPoseStack, 1, height, 0, sprite.getU0(), sprite.getV0(), tint);
//            add(builder, pPoseStack, 1, 0, 1, sprite.getU0(), sprite.getV1(), tint);
//            add(builder, pPoseStack, 0, 0, 1, sprite.getU1(), sprite.getV1(), tint);
//            add(builder, pPoseStack, 0, 0, 0, sprite.getU1(), sprite.getV0(), tint);
//            add(builder, pPoseStack, 1, 0, 0, sprite.getU0(), sprite.getV0(), tint);
//
//            // Back Faces
//            add(builder, pPoseStack, 1, height, 0, sprite.getU0(), sprite.getV0(), tint);
//            add(builder, pPoseStack, 0, height, 0, sprite.getU1(), sprite.getV0(), tint);
//            add(builder, pPoseStack, 0, 0, 0, sprite.getU1(), sprite.getV1(), tint);
//            add(builder, pPoseStack, 1, 0, 0, sprite.getU0(), sprite.getV1(), tint);
//
//            add(builder, pPoseStack, 1, 0, 1, sprite.getU0(), sprite.getV1(), tint);
//            add(builder, pPoseStack, 0, 0, 1, sprite.getU1(), sprite.getV1(), tint);
//            add(builder, pPoseStack, 0, height, 1, sprite.getU1(), sprite.getV0(), tint);
//            add(builder, pPoseStack, 1, height, 1, sprite.getU0(), sprite.getV0(), tint);
            pPoseStack.popPose();
        }
    }

    private static void add(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float u, float v, int tint) {
        renderer.addVertex(stack.last().pose(), x, y, z).setColor(tint).setUv(u, v).setUv2(0, 200).setNormal(1, 0, 0); // pV: Brightness
    }
}
