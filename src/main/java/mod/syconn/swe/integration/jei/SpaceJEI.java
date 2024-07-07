package mod.syconn.swe.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;
import mod.syconn.swe.Main;
import mod.syconn.swe.world.crafting.DyedParachuteRecipe;
import mod.syconn.swe.world.crafting.RefillingCanisterRecipe;

@JeiPlugin
public class SpaceJEI implements IModPlugin {

    public ResourceLocation getPluginUid() {
        return Main.loc("crafting");
    }

    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(DyedParachuteRecipe.class, new ParachuteRecipeWrapper());
        registration.getCraftingCategory().addExtension(RefillingCanisterRecipe.class, new CanisterRecipeWrapper());
    }
}
