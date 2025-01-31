package mod.syconn.swe.datagen;

import mod.syconn.swe.core.ModBlocks;
import mod.syconn.swe.core.ModItems;
import mod.syconn.swe.core.ModRecipes;
import mod.syconn.swe.core.ModTags;
import mod.syconn.swe.server.recipes.CustomRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class RecipeGen extends FabricRecipeProvider {

    public RecipeGen(FabricDataOutput output) {
        super(output);
    }

    public void buildRecipes(Consumer<FinishedRecipe> writer) {
        CustomRecipeBuilder.special(ModRecipes.PARACHUTE_RECIPE.get()).save(writer, "dyed_parachute", RecipeCategory.TOOLS, has(ItemTags.WOOL));
        CustomRecipeBuilder.special(ModRecipes.REFILLING_CANISTER.get()).save(writer, "refill_canister", RecipeCategory.TOOLS, has(ModItems.CANISTER.get()));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CANISTER_FILLER.get())
                .pattern("ggg")
                .pattern("gig")
                .pattern("g g")
                .define('g', Items.GOLD_INGOT)
                .define('i', Items.IRON_BLOCK)
                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_BLOCK, Items.GOLD_INGOT).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NETHERITE_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.NETHERITE_INGOT)
                .define('r', ModItems.EMERALD_UPGRADE.get())
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.NETHERITE_INGOT).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.DIAMOND)
                .define('r', ModItems.GOLD_UPGRADE.get())
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.DIAMOND).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EMERALD_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.EMERALD)
                .define('r', ModItems.DIAMOND_UPGRADE.get())
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.EMERALD).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GOLD_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.GOLD_INGOT)
                .define('r', ModItems.IRON_UPGRADE.get())
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.GOLD_INGOT).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.IRON_INGOT)
                .define('r', Items.REDSTONE)
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_INGOT).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModBlocks.FLUID_PIPE.get(), 4)
                .pattern("nnn")
                .pattern("nrn")
                .pattern("nnn")
                .define('n', ModTags.Items.GLASS_PANES)
                .define('r', Items.BUCKET)
                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModBlocks.FLUID_TANK.get())
                .pattern("bbb")
                .pattern("nrn")
                .pattern("bbb")
                .define('n', ModTags.Items.GLASS_BLOCKS)
                .define('r', Items.BUCKET)
                .define('b', Items.IRON_BLOCK)
                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
                .save(writer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CANISTER.get(), 1)
                .pattern("nnn")
                .pattern("nrn")
                .pattern("nnn")
                .define('n', Items.IRON_INGOT)
                .define('r', Items.BUCKET)
                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
                .save(writer);
    }
}
