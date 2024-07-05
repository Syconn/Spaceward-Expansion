package mod.syconn.swe.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;
import mod.syconn.swe.Main;
import mod.syconn.swe.world.crafting.DyedParachuteRecipe;
import mod.syconn.swe.world.crafting.RefillingCanisterRecipe;

@JeiPlugin
public class SpaceJEI implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(Main.MODID, "crafting");
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addCategoryExtension(DyedParachuteRecipe.class, ParachuteRecipeWrapper::new);
        registration.getCraftingCategory().addCategoryExtension(RefillingCanisterRecipe.class, CanisterRecipeWrapper::new);
    }
}
