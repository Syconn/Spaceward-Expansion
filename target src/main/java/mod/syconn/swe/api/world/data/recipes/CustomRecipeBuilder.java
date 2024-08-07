package mod.syconn.api.world.data.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Recipe;

import java.util.function.Function;

public class CustomRecipeBuilder {

    private final Function<CraftingBookCategory, Recipe<?>> factory;

    public CustomRecipeBuilder(Function<CraftingBookCategory, Recipe<?>> pFactory) {
        this.factory = pFactory;
    }

    public static CustomRecipeBuilder special(Function<CraftingBookCategory, Recipe<?>> pFactory) {
        return new CustomRecipeBuilder(pFactory);
    }

    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId, Criterion<?> pCriterion) {
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        advancement$builder.addCriterion("requirement", pCriterion);
        pRecipeOutput.accept(pId, this.factory.apply(CraftingBookCategory.MISC), advancement$builder.build(pId.withPrefix("recipes/misc/")));
    }
}
