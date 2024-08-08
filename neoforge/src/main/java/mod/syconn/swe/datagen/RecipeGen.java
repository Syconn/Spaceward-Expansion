package mod.syconn.swe.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public class RecipeGen extends RecipeProvider {

    public RecipeGen(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    protected void buildRecipes(RecipeOutput pRecipeOutput) {
//        CustomRecipeBuilder.special(DyedParachuteRecipe::new).save(pRecipeOutput, Main.loc("dyed_parachute"), has(ItemTags.WOOL));
//        CustomRecipeBuilder.special(RefillingCanisterRecipe::new).save(pRecipeOutput, Main.loc("refill_canister"), has(Registration.CANISTER));
//        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Registration.CANISTER_FILLER.get())
//                .pattern("ggg")
//                .pattern("gig")
//                .pattern("g g")
//                .define('g', Items.GOLD_INGOT)
//                .define('i', Items.IRON_BLOCK)
//                .unlockedBy("has_mats", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_BLOCK, Items.GOLD_INGOT).build()))
//                .save(pRecipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.NETHERITE_UPGRADE.get())
//                .pattern(" n ")
//                .pattern("nrn")
//                .pattern(" n ")
//                .define('n', Items.NETHERITE_INGOT)
//                .define('r', Registration.EMERALD_UPGRADE.get())
//                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.NETHERITE_INGOT).build()))
//                .save(pRecipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.DIAMOND_UPGRADE.get())
//                .pattern(" n ")
//                .pattern("nrn")
//                .pattern(" n ")
//                .define('n', Items.DIAMOND)
//                .define('r', Registration.GOLD_UPGRADE.get())
//                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.DIAMOND).build()))
//                .save(pRecipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.EMERALD_UPGRADE.get())
//                .pattern(" n ")
//                .pattern("nrn")
//                .pattern(" n ")
//                .define('n', Items.EMERALD)
//                .define('r', Registration.DIAMOND_UPGRADE.get())
//                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.EMERALD).build()))
//                .save(pRecipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.GOLD_UPGRADE.get())
//                .pattern(" n ")
//                .pattern("nrn")
//                .pattern(" n ")
//                .define('n', Items.GOLD_INGOT)
//                .define('r', Registration.IRON_UPGRADE.get())
//                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.GOLD_INGOT).build()))
//                .save(pRecipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.IRON_UPGRADE.get())
//                .pattern(" n ")
//                .pattern("nrn")
//                .pattern(" n ")
//                .define('n', Items.IRON_INGOT)
//                .define('r', Items.REDSTONE)
//                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_INGOT).build()))
//                .save(pRecipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Registration.FLUID_PIPE.get(), 4)
//                .pattern("nnn")
//                .pattern("nrn")
//                .pattern("nnn")
//                .define('n', Tags.Items.GLASS_PANES)
//                .define('r', Items.BUCKET)
//                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
//                .save(pRecipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Registration.FLUID_TANK.get())
//                .pattern("bbb")
//                .pattern("nrn")
//                .pattern("bbb")
//                .define('n', Tags.Items.GLASS_BLOCKS)
//                .define('r', Items.BUCKET)
//                .define('b', Items.IRON_BLOCK)
//                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
//                .save(pRecipeOutput);
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Registration.WRENCH.get())
//                .requires(Items.IRON_INGOT, 2)
//                .requires(Items.GOLD_INGOT)
//                .unlockedBy("has_ingot", inventoryTrigger(ItemPredicate.Builder.item().of(Items.IRON_INGOT).build()))
//                .save(pRecipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Registration.CANISTER.get(), 1)
//                .pattern("nnn")
//                .pattern("nrn")
//                .pattern("nnn")
//                .define('n', Items.IRON_INGOT)
//                .define('r', Items.BUCKET)
//                .unlockedBy("has_bucket", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BUCKET).build()))
//                .save(pRecipeOutput);
    }
}
