package mod.syconn.swe.init;

import mod.syconn.swe.data.recipes.DyedParachuteRecipe;
import mod.syconn.swe.data.recipes.RefillingCanisterRecipe;
import mod.syconn.swe.platform.Services;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public class RecipeSerializers {

    public static final Supplier<RecipeSerializer<DyedParachuteRecipe>> PARACHUTE_RECIPE = register("parachute_recipe", () -> new SimpleCraftingRecipeSerializer<>(DyedParachuteRecipe::new));
    public static final Supplier<RecipeSerializer<RefillingCanisterRecipe>> REFILLING_CANISTER = register("refilling_canister_recipe", () -> new SimpleCraftingRecipeSerializer<>(RefillingCanisterRecipe::new));

    public static void init() {}

    private static <T extends Recipe<?>> Supplier<RecipeSerializer<T>> register(String id, Supplier<RecipeSerializer<T>> recipeSerializer) {
        return Services.REGISTRAR.registerRecipeSerializer(id, recipeSerializer);
    }
}
