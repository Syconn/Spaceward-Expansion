package mod.syconn.swe.server.recipes;

import com.google.gson.JsonObject;
import mod.syconn.swe.Constants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Consumer;

public class CustomRecipeBuilder extends CraftingRecipeBuilder {
    final RecipeSerializer<?> serializer;

    public CustomRecipeBuilder(RecipeSerializer<?> serializer) {
        this.serializer = serializer;
    }

    public static CustomRecipeBuilder special(RecipeSerializer<? extends CraftingRecipe> serializer) {
        return new CustomRecipeBuilder(serializer);
    }

    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, String id, RecipeCategory category, InventoryChangeTrigger.TriggerInstance trigger) {
        Advancement.Builder advancement = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(Constants.withId(id)))
                .rewards(AdvancementRewards.Builder.recipe(Constants.withId(id)))
                .requirements(RequirementsStrategy.OR);
        advancement.addCriterion("requirement", trigger);
        finishedRecipeConsumer.accept(new CraftingRecipeBuilder.CraftingResult(determineBookCategory(category)) {
            public RecipeSerializer<?> getType() {
                return CustomRecipeBuilder.this.serializer;
            }
            public ResourceLocation getId() {
                return Constants.withId(id);
            }
            public JsonObject serializeAdvancement() {
                return advancement.serializeToJson();
            }
            public ResourceLocation getAdvancementId() {
                return Constants.withId(id).withPrefix("recipes/" + category.getFolderName() + "/");
            }
        });
    }
}