package mod.syconn.swe.extra.data.recipes;

import mod.syconn.swe.extra.util.ColorUtil;
import mod.syconn.swe.init.ItemRegister;
import mod.syconn.swe.init.RecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class DyedParachuteRecipe extends CustomRecipe {

    public DyedParachuteRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    public boolean matches(CraftingInput pInput, Level pLevel) {
        if (pInput.width() == 3 && pInput.height() == 3) {
            for (int i = 0; i < 3; i++) if (!pInput.getItem(i).is(ItemTags.WOOL)) return false;
            return pInput.getItem(3).getItem() == Items.STRING && pInput.getItem(5).getItem() == Items.STRING && pInput.getItem(7).getItem() == Items.STRING;
        } else {
            return false;
        }
    }

    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pProvider) {
        if (pInput.width() == 3 && pInput.height() == 3) {
            for (int i = 0; i < 3; i++) {
                if (!pInput.getItem(i).is(ItemTags.WOOL)) return ItemStack.EMPTY;
            }
            if (pInput.getItem(3).getItem() == Items.STRING && pInput.getItem(5).getItem() == Items.STRING && pInput.getItem(7).getItem() == Items.STRING) {
                List<DyeItem> colors = new ArrayList<>();
                for (int i = 0; i < 3; i++) colors.add(ColorUtil.DYE_BY_WOOL.get(pInput.getItem(i).getItem()));
                return DyedItemColor.applyDyes(new ItemStack(ItemRegister.PARACHUTE.get()), colors);
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return ItemRegister.PARACHUTE.get().getDefaultInstance();
    }

    public boolean canCraftInDimensions(int width, int height)
    {
        return width > 2 && height > 2;
    }

    public RecipeSerializer<?> getSerializer()
    {
        return RecipeSerializers.PARACHUTE_RECIPE.get();
    }
}