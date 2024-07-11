package mod.syconn.swe.util;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.NativeImage;
import mod.syconn.swe.Registration;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.awt.*;
import java.util.*;
import java.util.List;

import static mod.syconn.swe.util.ResourceUtil.getStillFluidSprite;

public class ColorUtil {

    public static final Map<ItemLike, DyeItem> DYE_BY_WOOL = Util.make(Maps.newHashMap(), map -> {
        map.put(Items.WHITE_WOOL, (DyeItem) Items.WHITE_DYE);
        map.put(Items.ORANGE_WOOL, (DyeItem) Items.ORANGE_DYE);
        map.put(Items.MAGENTA_WOOL, (DyeItem) Items.MAGENTA_DYE);
        map.put(Items.LIGHT_BLUE_WOOL, (DyeItem) Items.LIGHT_BLUE_DYE);
        map.put(Items.YELLOW_WOOL, (DyeItem) Items.YELLOW_DYE);
        map.put(Items.LIME_WOOL, (DyeItem) Items.LIME_DYE);
        map.put(Items.PINK_WOOL, (DyeItem) Items.PINK_DYE);
        map.put(Items.GRAY_WOOL, (DyeItem) Items.GRAY_DYE);
        map.put(Items.LIGHT_GRAY_WOOL, (DyeItem) Items.LIGHT_GRAY_DYE);
        map.put(Items.CYAN_WOOL, (DyeItem) Items.CYAN_DYE);
        map.put(Items.PURPLE_WOOL, (DyeItem) Items.PURPLE_DYE);
        map.put(Items.BLUE_WOOL, (DyeItem) Items.BLUE_DYE);
        map.put(Items.BROWN_WOOL, (DyeItem) Items.BROWN_DYE);
        map.put(Items.GREEN_WOOL, (DyeItem) Items.GREEN_DYE);
        map.put(Items.RED_WOOL, (DyeItem) Items.RED_DYE);
        map.put(Items.BLACK_WOOL, (DyeItem) Items.BLACK_DYE);
    });

    public static int getColor(FluidStack fluid){
        if (fluid.is(Registration.O2.get())) return 0xFF3F76E4;
        if (!fluid.is(Fluids.EMPTY)) {
            int i = IClientFluidTypeExtensions.of(fluid.getFluidType()).getTintColor(fluid);
            NativeImage image = ResourceUtil.getMainImage(getStillFluidSprite(fluid).get()).get();

            Minecraft.getInstance().getBlockRenderer();
//            if (i != 0xFFFFFFFF) return i;
//            System.out.println(ResourceUtil.getMainImage(getStillFluidSprite(fluid).get()).get().getPixelRGBA(8, 8));

//            System.out.println(image.getRedOrLuminance(2, 8) + " " + image.getGreenOrLuminance(8, 8) + " " + image.getBlueOrLuminance(8, 8));
            return FastColor.as8BitChannel(image.getPixelRGBA(8, 8));
//            return image.getPixelRGBA(0, 0);
        }
        return -1;
    }

    public List<Integer> getColors(TextureAtlasSprite textureAtlasSprite, int renderColor, int colorCount) {
        if (colorCount <= 0) {
            return Collections.emptyList();
        }
        return ResourceUtil.getMainImage(textureAtlasSprite)
                .map(bufferedImage -> {
                    final List<Integer> colors = new ArrayList<>(colorCount);
                    final int[][] palette = getPixels(bufferedImage, 2, false);
                    for (int[] colorInt : palette) {
                        int red = (int) ((colorInt[0] - 1) * (float) (renderColor >> 16 & 255) / 255.0F);
                        int green = (int) ((colorInt[1] - 1) * (float) (renderColor >> 8 & 255) / 255.0F);
                        int blue = (int) ((colorInt[2] - 1) * (float) (renderColor & 255) / 255.0F);
                        red = Mth.clamp(red, 0, 255);
                        green = Mth.clamp(green, 0, 255);
                        blue = Mth.clamp(blue, 0, 255);
                        int color = ((0xFF) << 24) |
                                ((red & 0xFF) << 16) |
                                ((green & 0xFF) << 8) |
                                (blue & 0xFF);
                        colors.add(color);
                    }
                    return colors;
                })
                .orElseGet(Collections::emptyList);
    }

    private static int[][] getPixels(NativeImage sourceImage, int quality, boolean ignoreWhite) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int pixelCount = width * height;
        int numRegardedPixels = (pixelCount + quality - 1) / quality;

        int numUsedPixels = 0;
        int[][] pixelArray = new int[numRegardedPixels][];

        int i = 0;
        while (i < pixelCount) {
            int x = i % width;
            int y = i / width;
            int rgba = sourceImage.getPixelRGBA(x, y);
            int a = rgba >> 24 & 255;
            int b = rgba >> 16 & 255;
            int g = rgba >> 8 & 255;
            int r = rgba & 255;
            if (a >= 125 && !(ignoreWhite && r > 250 && g > 250 && b > 250)) {
                pixelArray[numUsedPixels] = new int[]{r, g, b};
                numUsedPixels++;
                i += quality;
            } else {
                i++;
            }
        }
        // trim the array
        return Arrays.copyOfRange(pixelArray, 0, numUsedPixels);
    }

    public static DyeColor getClosetColor(int c){
        int closetNum = -1;
        DyeColor closetColor = DyeColor.WHITE;
        for (DyeColor color : DyeColor.values()){
            Color lColor = new Color(c);
            Color input = new Color(color.getFireworkColor());
            int checkNumR = Math.abs(input.getRed() - lColor.getRed());
            int checkNumG = Math.abs(input.getGreen() - lColor.getGreen());
            int checkNumB = Math.abs(input.getBlue() - lColor.getBlue());
            int checkNum = checkNumR + checkNumG + checkNumB;

            if (closetNum == -1) {
                closetNum = checkNum;
                closetColor = color;
            }
            //CHECKS SMALLEST DISTANCE VAL
            else if (closetNum > checkNum) {
                closetNum = checkNum;
                closetColor = color;
            }
        }
        return closetColor;
    }

    public static DyeColor byMaterialColor(MapColor color) {
        for (DyeColor c : DyeColor.values()){
            if (c.getMapColor() == color) return c;
        }
        return DyeColor.WHITE;
    }
}
