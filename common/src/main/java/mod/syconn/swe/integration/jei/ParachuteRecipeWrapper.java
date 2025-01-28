package mod.syconn.swe.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mod.syconn.swe.core.ModItems;
import mod.syconn.swe.server.recipes.DyedParachuteRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ParachuteRecipeWrapper implements ICraftingCategoryExtension {

    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        List<ItemStack> input = new ArrayList<>();
        List<ItemStack> outputs = new ArrayList<>();
        List<ItemStack> string = ImmutableList.of(new ItemStack(Items.STRING));
        List<ItemStack> empty = ImmutableList.of(ItemStack.EMPTY);
        BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.WOOL).forEach(stack -> input.add(new ItemStack(stack)));
        for (ItemStack stack : input)
            if (DyedParachuteRecipe.DYE_BY_WOOL.containsKey(stack.getItem()))
                outputs.add(DyeableLeatherItem.dyeArmor(new ItemStack(ModItems.PARACHUTE.get()), List.of(DyedParachuteRecipe.DYE_BY_WOOL.get(stack.getItem()))));
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
