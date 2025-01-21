package mod.syconn.swe.server.recipes;

import mod.syconn.swe.core.ModItems;
import mod.syconn.swe.core.ModRecipes;
import mod.syconn.swe.util.ColorUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class DyedParachuteRecipe extends CustomRecipe {

    public DyedParachuteRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return ModItems.PARACHUTE.get().getDefaultInstance();
    }

    public boolean matches(CraftingContainer container, Level level) {
        if (container.getWidth() == 3 && container.getHeight() == 3) {
            for (int i = 0; i < 3; i++) if (!container.getItem(i).is(ItemTags.WOOL)) return false;
            return container.getItem(3).getItem() == Items.STRING && container.getItem(5).getItem() == Items.STRING && container.getItem(7).getItem() == Items.STRING;
        } else return false;
    }

    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        if (container.getWidth() == 3 && container.getHeight() == 3) {
            for (int i = 0; i < 3; i++) if (!container.getItem(i).is(ItemTags.WOOL)) return ItemStack.EMPTY;
            if (container.getItem(3).getItem() == Items.STRING && container.getItem(5).getItem() == Items.STRING && container.getItem(7).getItem() == Items.STRING) {
                List<DyeItem> colors = new ArrayList<>();
                for (int i = 0; i < 3; i++) colors.add(ColorUtil.DYE_BY_WOOL.get(container.getItem(i).getItem()));
                return DyeableLeatherItem.dyeArmor(new ItemStack(ModItems.PARACHUTE.get()), colors);
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return new ItemStack(ModItems.PARACHUTE.get());
    }

    public boolean canCraftInDimensions(int width, int height)
    {
        return width > 2 && height > 2;
    }

    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipes.PARACHUTE_RECIPE.get();
    }
}