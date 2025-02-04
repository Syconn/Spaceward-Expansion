package mod.syconn.swe.server.recipes;

import dev.architectury.fluid.FluidStack;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.registry.ModItems;
import mod.syconn.swe.registry.ModRecipes;
import mod.syconn.swe.util.FluidUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RefillingCanisterRecipe extends CustomRecipe {

    public RefillingCanisterRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    public boolean matches(CraftingContainer container, Level level) {
        if (container.getWidth() == 3 && container.getHeight() == 3) {
            for (int i = 0; i < 9; i++) {
                if (i % 2 == 0 && i != 4 && !container.getItem(i).is(ModItems.GOLD_UPGRADE.get())) return false;
                if (i % 2 != 0 && !container.getItem(i).is(Items.GOLD_INGOT)) return false;
            }
            return container.getItem(4).is(ModItems.CANISTER.get());
        }
        return false;
    }

    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        if (container.getWidth() == 3 && container.getHeight() == 3) {
            for (int i = 0; i < 9; i++) {
                if (i % 2 == 0 && i != 4 && !container.getItem(i).is(ModItems.GOLD_UPGRADE.get())) return ItemStack.EMPTY;
                if (i % 2 != 0 && !container.getItem(i).is(Items.GOLD_INGOT)) return ItemStack.EMPTY;
            }
            if (container.getItem(4).is(ModItems.CANISTER.get())) {
                return FluidUtil.createFluidItem(ModItems.AUTO_REFILL_CANISTER.get(), FluidHolderItem.getViewOnly(container.getItem(4)).getFluidStack());
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return FluidUtil.createFluidItem(ModItems.AUTO_REFILL_CANISTER.get(), FluidStack.empty());
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width > 2 && height > 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.REFILLING_CANISTER.get();
    }
}
