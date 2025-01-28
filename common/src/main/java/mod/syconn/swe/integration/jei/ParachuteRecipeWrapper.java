package mod.syconn.swe.integration.jei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mod.syconn.swe.core.ModItems;
import mod.syconn.swe.util.ColorUtil;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParachuteRecipeWrapper implements ICraftingCategoryExtension {

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

    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        List<ItemStack> input = new ArrayList<>();
        List<ItemStack> outputs = new ArrayList<>();
        List<ItemStack> string = ImmutableList.of(new ItemStack(Items.STRING));
        List<ItemStack> empty = ImmutableList.of(ItemStack.EMPTY);
        BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.WOOL).forEach(stack -> input.add(new ItemStack(stack)));
        for (ItemStack stack : input) if (DYE_BY_WOOL.containsKey(stack.getItem())) outputs.add(DyeableLeatherItem.dyeArmor(new ItemStack(ModItems.PARACHUTE.get()), List.of(DYE_BY_WOOL.get(stack.getItem()))));
        List<List<ItemStack>> inputs = ImmutableList.of(input, input, input, string, empty, string, empty, string, empty);
        craftingGridHelper.createAndSetInputs(builder, inputs, getWidth(), getHeight());
        craftingGridHelper.createAndSetOutputs(builder, outputs);
    }

    public int getWidth() {
        return 3;
    }

    public int getHeight() {
        return 3;
    }
}
