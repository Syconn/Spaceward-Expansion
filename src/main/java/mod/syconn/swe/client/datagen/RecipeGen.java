package mod.syconn.swe.client.datagen;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import mod.syconn.swe.world.crafting.DyedParachuteRecipe;
import mod.syconn.swe.world.crafting.RefillingCanisterRecipe;

import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider {

    public RecipeGen(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new DyedParachuteRecipe.Result());
        consumer.accept(new RefillingCanisterRecipe.Result());
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModInit.CANISTER_FILLER.get())
                .pattern("ggg")
                .pattern("gig")
                .pattern("g g")
                .define('g', Items.GOLD_INGOT)
                .define('i', Items.IRON_BLOCK)
                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_BLOCK, Items.GOLD_INGOT).build()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModInit.NETHERITE_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.NETHERITE_INGOT)
                .define('r', ModInit.EMERALD_UPGRADE.get())
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.NETHERITE_INGOT).build()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModInit.DIAMOND_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.DIAMOND)
                .define('r', ModInit.GOLD_UPGRADE.get())
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.DIAMOND).build()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModInit.EMERALD_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.EMERALD)
                .define('r', ModInit.DIAMOND_UPGRADE.get())
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.EMERALD).build()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModInit.GOLD_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.GOLD_INGOT)
                .define('r', ModInit.IRON_UPGRADE.get())
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.GOLD_INGOT).build()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModInit.IRON_UPGRADE.get())
                .pattern(" n ")
                .pattern("nrn")
                .pattern(" n ")
                .define('n', Items.IRON_INGOT)
                .define('r', Items.REDSTONE)
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_INGOT).build()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModInit.FLUID_PIPE.get(), 16)
                .pattern("nnn")
                .pattern("nrn")
                .pattern("nnn")
                .define('n', Tags.Items.GLASS_PANES)
                .define('r', Items.BUCKET)
                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModInit.FLUID_TANK.get())
                .pattern("bbb")
                .pattern("nrn")
                .pattern("bbb")
                .define('n', ModTags.GLASS)
                .define('r', Items.BUCKET)
                .define('b', Items.IRON_BLOCK)
                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInit.WRENCH.get())
                .requires(Items.IRON_INGOT, 2)
                .requires(Items.GOLD_INGOT)
                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_INGOT).build()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModInit.CANISTER.get(), 16)
                .pattern("nnn")
                .pattern("nrn")
                .pattern("nnn")
                .define('n', Items.IRON_INGOT)
                .define('r', Items.BUCKET)
                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
                .save(consumer);
    }
}
