package mod.syconn.swe.extra.data.recipes;

import mod.syconn.swe.init.ComponentRegister;
import mod.syconn.swe.init.ItemRegister;
import mod.syconn.swe.init.RecipeSerializers;
import mod.syconn.swe.items.Canister;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RefillingCanisterRecipe extends CustomRecipe {

    public RefillingCanisterRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    public boolean matches(CraftingInput pInput, Level pLevel) {
        if (pInput.width() == 3 && pInput.height() == 3) {
            for (int i = 0; i < 9; i++) {
                if (i % 2 == 0 && i != 4 && !pInput.getItem(i).is(ItemRegister.GOLD_UPGRADE.get())) return false;
                if (i % 2 != 0 && !pInput.getItem(i).is(Items.GOLD_INGOT)) return false;
            }
            return pInput.getItem(4).is(ItemRegister.CANISTER.get());
        }
        return false;
    }

    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pProvider) {
        if (pInput.width() == 3 && pInput.height() == 3) {
            for (int i = 0; i < 9; i++) {
                if (i % 2 == 0 && i != 4 && !pInput.getItem(i).is(ItemRegister.GOLD_UPGRADE.get())) return ItemStack.EMPTY;
                if (i % 2 != 0 && !pInput.getItem(i).is(Items.GOLD_INGOT)) return ItemStack.EMPTY;
            }
            if (pInput.getItem(4).is(ItemRegister.CANISTER.get())) {
                ItemStack result = new ItemStack(ItemRegister.AUTO_REFILL_CANISTER.get());
                result.set(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), pInput.getItem(4).get(ComponentRegister.FLUID_HOLDER_COMPONENT.get()));
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return Canister.createEmpty(ItemRegister.CANISTER.get());
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width > 2 && height > 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.REFILLING_CANISTER.get();
    }
}
