package mod.syconn.swe.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import mod.syconn.swe.Main;
import mod.syconn.swe.common.crafting.DyedParachuteRecipe;
import mod.syconn.swe.common.crafting.RefillingCanisterRecipe;

public class ModRecipeSerializer {

    public static final DeferredRegister<RecipeSerializer<?>> REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Main.MODID);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<DyedParachuteRecipe>> PARACHUTE_RECIPE = REGISTER.register("parachute_recipe", () -> new SimpleCraftingRecipeSerializer<>(DyedParachuteRecipe::new));

    public static final RegistryObject<SimpleCraftingRecipeSerializer<RefillingCanisterRecipe>> REFILLING_CANISTER = REGISTER.register("refilling_canister_recipe", () -> new SimpleCraftingRecipeSerializer<>(RefillingCanisterRecipe::new));
}
