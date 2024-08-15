package mod.syconn.swe.extra.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mod.syconn.swe.extra.data.recipes.DyedParachuteRecipe;
import mod.syconn.swe.init.ItemRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

import static mod.syconn.swe.extra.util.ColorUtil.DYE_BY_WOOL;

public class ParachuteRecipeWrapper implements ICraftingCategoryExtension<DyedParachuteRecipe> {

    public void setRecipe(RecipeHolder recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        List<ItemStack> input = new ArrayList<>();
        List<ItemStack> outputs = new ArrayList<>();
        List<ItemStack> string = ImmutableList.of(new ItemStack(Items.STRING));
        List<ItemStack> empty = ImmutableList.of(ItemStack.EMPTY);
        BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.WOOL).forEach(stack -> input.add(new ItemStack(stack)));
        for (ItemStack stack : input) if (DYE_BY_WOOL.containsKey(stack.getItem())) outputs.add(DyedItemColor.applyDyes(new ItemStack(ItemRegister.PARACHUTE.get()), List.of(DYE_BY_WOOL.get(stack.getItem()))));
        List<List<ItemStack>> inputs = ImmutableList.of(input, input, input, string, empty, string, empty, string, empty);
        craftingGridHelper.createAndSetInputs(builder, inputs, getWidth(recipeHolder), getHeight(recipeHolder));
        craftingGridHelper.createAndSetOutputs(builder, outputs);
    }

    public int getWidth(RecipeHolder recipeHolder) {
        return 3;
    }

    public int getHeight(RecipeHolder recipeHolder) {
        return 3;
    }
}
