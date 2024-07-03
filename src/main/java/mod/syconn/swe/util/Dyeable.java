package mod.syconn.swe.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MaterialColor;

import java.util.List;

public interface Dyeable {

    String COLOR = "color";

    static boolean hasColor(ItemStack t){
        return t.getOrCreateTag().contains(COLOR);
    }

    static int getColor(ItemStack t){
        return t.getOrCreateTag().getInt(COLOR);
    }

    static void setColor(ItemStack t, int c){
        t.getOrCreateTag().putInt(COLOR, c);
    }

    static ItemStack dye(ItemStack stack, List<MaterialColor> list) {
        ItemStack resultStack = stack.copy();
        int[] combinedColors = new int[3];
        int maxColor = 0;
        int colorCount = 0;
        resultStack.setCount(1);
        if(hasColor(stack))
        {
            int color = getColor(resultStack);
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;
            maxColor = (int) ((float) maxColor + Math.max(red, Math.max(green, blue)) * 255.0F);
            combinedColors[0] = (int) ((float) combinedColors[0] + red * 255.0F);
            combinedColors[1] = (int) ((float) combinedColors[1] + green * 255.0F);
            combinedColors[2] = (int) ((float) combinedColors[2] + blue * 255.0F);
            colorCount++;
        }

        for(MaterialColor matC : list)
        {
            float[] colorComponents = ColorUtil.byMaterialColor(matC).getTextureDiffuseColors();
            int red = (int) (colorComponents[0] * 255.0F);
            int green = (int) (colorComponents[1] * 255.0F);
            int blue = (int) (colorComponents[2] * 255.0F);
            maxColor += Math.max(red, Math.max(green, blue));
            combinedColors[0] += red;
            combinedColors[1] += green;
            combinedColors[2] += blue;
            colorCount++;
        }

        int red = combinedColors[0] / colorCount;
        int green = combinedColors[1] / colorCount;
        int blue = combinedColors[2] / colorCount;
        float averageColor = (float) maxColor / (float) colorCount;
        float maxValue = (float) Math.max(red, Math.max(green, blue));
        red = (int) ((float) red * averageColor / maxValue);
        green = (int) ((float) green * averageColor / maxValue);
        blue = (int) ((float) blue * averageColor / maxValue);
        int finalColor = (red << 8) + green;
        finalColor = (finalColor << 8) + blue;
        setColor(resultStack, finalColor);
        return resultStack;
    }
}
