package mod.syconn.swe.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mod.syconn.swe.Constants;
import mod.syconn.swe.common.crafting.DyedParachuteRecipe;
import mod.syconn.swe.common.crafting.RefillingCanisterRecipe;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class SpaceJEI implements IModPlugin {

    public ResourceLocation getPluginUid() {
        return Constants.loc("crafting");
    }

    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(DyedParachuteRecipe.class, new ParachuteRecipeWrapper());
        registration.getCraftingCategory().addExtension(RefillingCanisterRecipe.class, new CanisterRecipeWrapper());
    }
}
