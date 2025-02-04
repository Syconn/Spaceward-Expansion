package mod.syconn.swe.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mod.syconn.swe.registry.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class CanisterRecipeWrapper implements ICraftingCategoryExtension {

    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        List<ItemStack> gold = ImmutableList.of(new ItemStack(Items.GOLD_INGOT));
        List<ItemStack> upgrade = ImmutableList.of(new ItemStack(ModItems.GOLD_UPGRADE.get()));
        List<List<ItemStack>> inputs = ImmutableList.of(upgrade, gold, upgrade, gold, List.of(new ItemStack(ModItems.CANISTER.get())), gold, upgrade, gold, upgrade);
        craftingGridHelper.createAndSetInputs(builder, inputs, getWidth(), getHeight());
        craftingGridHelper.createAndSetOutputs(builder, List.of(new ItemStack(ModItems.AUTO_REFILL_CANISTER.get())));
    }

    public int getHeight() {
        return 3;
    }

    public int getWidth() {
        return 3;
    }
}
