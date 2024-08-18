package mod.syconn.swe.extra.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mod.syconn.swe.Constants;
import mod.syconn.swe.extra.data.recipes.DyedParachuteRecipe;
import mod.syconn.swe.extra.data.recipes.RefillingCanisterRecipe;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class SpaceJEI implements IModPlugin {

//    ISubtypeRegistration TODO

    public ResourceLocation getPluginUid() {
        return Constants.loc("crafting");
    }

    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(DyedParachuteRecipe.class, new ParachuteRecipeWrapper());
        registration.getCraftingCategory().addExtension(RefillingCanisterRecipe.class, new CanisterRecipeWrapper());
    }

    public void registerItemSubtypes(ISubtypeRegistration registration) {
        IModPlugin.super.registerItemSubtypes(registration);
    }
}
