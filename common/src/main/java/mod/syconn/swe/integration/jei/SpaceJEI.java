package mod.syconn.swe.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mod.syconn.swe.Constants;
import mod.syconn.swe.server.recipes.DyedParachuteRecipe;
import mod.syconn.swe.server.recipes.RefillingCanisterRecipe;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class SpaceJEI implements IModPlugin {

    public ResourceLocation getPluginUid() {
        return Constants.withId("crafting");
    }

    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addCategoryExtension(DyedParachuteRecipe.class, recipe -> new ParachuteRecipeWrapper());
        registration.getCraftingCategory().addCategoryExtension(RefillingCanisterRecipe.class, recipe -> new CanisterRecipeWrapper());
    }
}
