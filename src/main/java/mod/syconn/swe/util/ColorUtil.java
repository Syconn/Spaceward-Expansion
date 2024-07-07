package mod.syconn.swe.util;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;

import java.awt.*;

public class ColorUtil {

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
