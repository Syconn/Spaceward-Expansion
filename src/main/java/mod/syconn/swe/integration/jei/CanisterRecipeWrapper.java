package mod.syconn.swe.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import mod.syconn.swe.world.crafting.RefillingCanisterRecipe;

import javax.annotation.Nullable;
import java.util.List;

public class CanisterRecipeWrapper implements ICraftingCategoryExtension {

    private final ResourceLocation name;

    public CanisterRecipeWrapper(RefillingCanisterRecipe recipe) {
        this.name = recipe.getId();
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return name;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ICraftingGridHelper helper, @NotNull IFocusGroup focusGroup) {
        List<ItemStack> gold = ImmutableList.of(new ItemStack(Items.GOLD_INGOT));
        List<ItemStack> upgrade = ImmutableList.of(new ItemStack(ModInit.GOLD_UPGRADE.get()));
        List<List<ItemStack>> inputs = ImmutableList.of(upgrade, gold, upgrade, gold, List.of(new ItemStack(ModInit.CANISTER.get())), gold, upgrade, gold, upgrade);
        helper.createAndSetInputs(builder, inputs, getWidth(), getHeight());
        helper.createAndSetOutputs(builder, List.of(new ItemStack(ModInit.AUTO_REFILL_CANISTER.get())));
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
