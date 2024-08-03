package mod.syconn.swe2.util;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.MapColor;

import java.awt.*;
import java.util.Map;

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
            } else if (closetNum > checkNum) {
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
