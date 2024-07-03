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
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.MaterialColor;
import mod.syconn.swe.init.ModInit;
import mod.syconn.swe.init.ModRecipeSerializer;
import mod.syconn.swe.util.Dyeable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public class DyedParachuteRecipe extends CustomRecipe {

    // TODO DOESNT APPEAR IN CRAFTING TABLE BOOK
    public DyedParachuteRecipe(ResourceLocation id, CraftingBookCategory category)
    {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inventory, Level worldIn) {
        if (inventory.getWidth() == 3 && inventory.getHeight() == 3) {
            for (int i = 0; i < 3; i++) if (!inventory.getItem(i).is(ItemTags.WOOL)) return false;
            return inventory.getItem(3).getItem() == Items.STRING && inventory.getItem(5).getItem() == Items.STRING && inventory.getItem(7).getItem() == Items.STRING;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingContainer inventory, RegistryAccess access) {
        if (inventory.getWidth() == 3 && inventory.getHeight() == 3) {
            for (int i = 0; i < 3; i++) {
                if (!inventory.getItem(i).is(ItemTags.WOOL)) return ItemStack.EMPTY;
            }
            if (inventory.getItem(3).getItem() == Items.STRING && inventory.getItem(5).getItem() == Items.STRING && inventory.getItem(7).getItem() == Items.STRING) {
                List<MaterialColor> colors = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    colors.add(((BlockItem) inventory.getItem(i).getItem()).getBlock().defaultMaterialColor());
                }
                return Dyeable.dye(new ItemStack(ModInit.PARACHUTE.get()), colors);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width > 2 && height > 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return new ItemStack(ModInit.PARACHUTE.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializer.PARACHUTE_RECIPE.get();
    }

    public static class Result implements FinishedRecipe {
        public void serializeRecipeData(JsonObject p_125967_) {}
        public ResourceLocation getId() {
            return BuiltInRegistries.ITEM.getKey(ModInit.PARACHUTE.get());
        }
        public RecipeSerializer<?> getType() {
            return ModRecipeSerializer.PARACHUTE_RECIPE.get();
        }
        public JsonObject serializeAdvancement() {
            return Advancement.Builder.advancement().parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(getId())).rewards(AdvancementRewards.Builder.recipe(getId())).requirements(RequirementsStrategy.OR).addCriterion("needs_wool", inventoryTrigger(ItemPredicate.Builder.item().of(ItemTags.WOOL).build())).serializeToJson();
        }
        public ResourceLocation getAdvancementId() {
            return BuiltInRegistries.ITEM.getKey(ModInit.PARACHUTE.get()).withPrefix("recipes/" + RecipeCategory.COMBAT.getFolderName() + "/");
        }
        protected static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... p_126012_) {
            return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, p_126012_);
        }
    }
}