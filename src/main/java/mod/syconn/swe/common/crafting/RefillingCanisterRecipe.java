package mod.syconn.swe.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import mod.syconn.swe.init.ModInit;
import mod.syconn.swe.init.ModRecipeSerializer;
import mod.syconn.swe.item.Canister;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public class RefillingCanisterRecipe extends CustomRecipe {

    public RefillingCanisterRecipe(ResourceLocation p_252125_, CraftingBookCategory p_249010_) {
        super(p_252125_, p_249010_);
    }

    @Override
    public boolean matches(CraftingContainer inventory, Level p_44003_) {
        if (inventory.getWidth() == 3 && inventory.getHeight() == 3) {
            for (int i = 0; i < 9; i++) {
                if (i % 2 == 0 && i != 4 && !inventory.getItem(i).is(ModInit.GOLD_UPGRADE.get())) return false;
                if (i % 2 != 0 && !inventory.getItem(i).is(Items.GOLD_INGOT)) return false;
            }
            return inventory.getItem(4).is(ModInit.CANISTER.get());
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer inventory, RegistryAccess p_267165_) {
        if (inventory.getWidth() == 3 && inventory.getHeight() == 3) {
            for (int i = 0; i < 9; i++) {
                if (i % 2 == 0 && i != 4 && !inventory.getItem(i).is(ModInit.GOLD_UPGRADE.get())) return ItemStack.EMPTY;
                if (i % 2 != 0 && !inventory.getItem(i).is(Items.GOLD_INGOT)) return ItemStack.EMPTY;
            }
            if (inventory.getItem(4).is(ModInit.CANISTER.get())) {
                ItemStack result = new ItemStack(ModInit.AUTO_REFILL_CANISTER.get());
                Canister.copy(result, inventory.getItem(4));
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width > 2 && height > 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.REFILLING_CANISTER.get();
    }

    public static class Result implements FinishedRecipe {
        public void serializeRecipeData(JsonObject p_125967_) {}
        public ResourceLocation getId() {
            return BuiltInRegistries.ITEM.getKey(ModInit.AUTO_REFILL_CANISTER.get());
        }
        public RecipeSerializer<?> getType() {
            return ModRecipeSerializer.REFILLING_CANISTER.get();
        }
        public JsonObject serializeAdvancement() {
            return Advancement.Builder.advancement().parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(getId())).rewards(AdvancementRewards.Builder.recipe(getId())).requirements(RequirementsStrategy.OR).addCriterion("needs_gold", inventoryTrigger(ItemPredicate.Builder.item().of(Items.GOLD_INGOT).build())).serializeToJson();
        }
        public ResourceLocation getAdvancementId() {
            return BuiltInRegistries.ITEM.getKey(ModInit.AUTO_REFILL_CANISTER.get()).withPrefix("recipes/" + RecipeCategory.TOOLS.getFolderName() + "/");
        }
        protected static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... p_126012_) {
            return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, p_126012_);
        }
    }
}
