package mod.syconn.swe.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swe.server.recipes.DyedParachuteRecipe;
import mod.syconn.swe.server.recipes.RefillingCanisterRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import static mod.syconn.swe.Constants.MOD;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(MOD, Registries.RECIPE_SERIALIZER);

    public static final RegistrySupplier<RecipeSerializer<DyedParachuteRecipe>> PARACHUTE_RECIPE = RECIPE_SERIALIZERS.register("parachute_recipe", () -> new SimpleCraftingRecipeSerializer<>(DyedParachuteRecipe::new));
    public static final RegistrySupplier<RecipeSerializer<RefillingCanisterRecipe>> REFILLING_CANISTER = RECIPE_SERIALIZERS.register("refilling_canister_recipe", () -> new SimpleCraftingRecipeSerializer<>(RefillingCanisterRecipe::new));
}
