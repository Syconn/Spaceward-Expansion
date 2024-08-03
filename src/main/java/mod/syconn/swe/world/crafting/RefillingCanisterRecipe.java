package mod.syconn.swe2.world.crafting;

import mod.syconn.swe2.Registration;
import mod.syconn.swe2.items.Canister;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class RefillingCanisterRecipe extends CustomRecipe {

    public RefillingCanisterRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    public boolean matches(CraftingInput pInput, Level pLevel) {
        if (pInput.width() == 3 && pInput.height() == 3) {
            for (int i = 0; i < 9; i++) {
                if (i % 2 == 0 && i != 4 && !pInput.getItem(i).is(Registration.GOLD_UPGRADE.get())) return false;
                if (i % 2 != 0 && !pInput.getItem(i).is(Items.GOLD_INGOT)) return false;
            }
            return pInput.getItem(4).is(Registration.CANISTER.get());
        }
        return false;
    }

    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pProvider) {
        if (pInput.width() == 3 && pInput.height() == 3) {
            for (int i = 0; i < 9; i++) {
                if (i % 2 == 0 && i != 4 && !pInput.getItem(i).is(Registration.GOLD_UPGRADE.get())) return ItemStack.EMPTY;
                if (i % 2 != 0 && !pInput.getItem(i).is(Items.GOLD_INGOT)) return ItemStack.EMPTY;
            }
            if (pInput.getItem(4).is(Registration.CANISTER.get())) {
                ItemStack result = new ItemStack(Registration.AUTO_REFILL_CANISTER.get());
                result.set(Registration.FLUID_COMPONENT, pInput.getItem(4).get(Registration.FLUID_COMPONENT));
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
//        return Registration.CANISTER.get().create(FluidStack.EMPTY);
        return Registration.CANISTER.get().getDefaultInstance();
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width > 2 && height > 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return Registration.REFILLING_CANISTER.get();
    }
}
