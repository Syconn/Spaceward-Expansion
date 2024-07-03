package mod.syconn.swe.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import mod.syconn.swe.common.crafting.DyedParachuteRecipe;
import mod.syconn.swe.init.ModInit;
import mod.syconn.swe.util.Dyeable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ParachuteRecipeWrapper implements ICraftingCategoryExtension {

    private final ResourceLocation name;

    public ParachuteRecipeWrapper(DyedParachuteRecipe recipe) {
        this.name = recipe.getId();
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return name;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ICraftingGridHelper helper, @NotNull IFocusGroup focusGroup) {
        List<ItemStack> input = new ArrayList<>();
        List<ItemStack> outputs = new ArrayList<>();
        List<ItemStack> string = ImmutableList.of(new ItemStack(Items.STRING));
        List<ItemStack> empty = ImmutableList.of(ItemStack.EMPTY);
        BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.WOOL).forEach(stack -> input.add(new ItemStack(stack)));
        for (ItemStack stack : input) {
            ItemStack s = new ItemStack(ModInit.PARACHUTE.get());
            Dyeable.setColor(s, ((BlockItem) stack.getItem()).getBlock().defaultMaterialColor().col);
            outputs.add(s);
        }
        List<List<ItemStack>> inputs = ImmutableList.of(input, input, input, string, empty, string, empty, string, empty);
        helper.createAndSetInputs(builder, inputs, getWidth(), getHeight());
        helper.createAndSetOutputs(builder, outputs);
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public int getWidth() {
        return 3;
    }
}
