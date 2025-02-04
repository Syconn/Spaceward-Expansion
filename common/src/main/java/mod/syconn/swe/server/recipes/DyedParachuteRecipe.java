package mod.syconn.swe.server.recipes;

import com.google.common.collect.Maps;
import mod.syconn.swe.registry.ModItems;
import mod.syconn.swe.registry.ModRecipes;
import net.minecraft.Util;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DyedParachuteRecipe extends CustomRecipe {

    public static final Map<ItemLike, DyeItem> DYE_BY_WOOL = Util.make(Maps.newHashMap(), map -> {
        map.put(Items.WHITE_WOOL, (DyeItem) Items.WHITE_DYE);
        map.put(Items.ORANGE_WOOL, (DyeItem) Items.ORANGE_DYE);
        map.put(Items.MAGENTA_WOOL, (DyeItem) Items.MAGENTA_DYE);
        map.put(Items.LIGHT_BLUE_WOOL, (DyeItem) Items.LIGHT_BLUE_DYE);
        map.put(Items.YELLOW_WOOL, (DyeItem) Items.YELLOW_DYE);
        map.put(Items.LIME_WOOL, (DyeItem) Items.LIME_DYE);
        map.put(Items.PINK_WOOL, (DyeItem) Items.PINK_DYE);
        map.put(Items.GRAY_WOOL, (DyeItem) Items.GRAY_DYE);
        map.put(Items.LIGHT_GRAY_WOOL, (DyeItem) Items.LIGHT_GRAY_DYE);
        map.put(Items.CYAN_WOOL, (DyeItem) Items.CYAN_DYE);
        map.put(Items.PURPLE_WOOL, (DyeItem) Items.PURPLE_DYE);
        map.put(Items.BLUE_WOOL, (DyeItem) Items.BLUE_DYE);
        map.put(Items.BROWN_WOOL, (DyeItem) Items.BROWN_DYE);
        map.put(Items.GREEN_WOOL, (DyeItem) Items.GREEN_DYE);
        map.put(Items.RED_WOOL, (DyeItem) Items.RED_DYE);
        map.put(Items.BLACK_WOOL, (DyeItem) Items.BLACK_DYE);
    });

    public DyedParachuteRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
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
                for (int i = 0; i < 3; i++) colors.add(DYE_BY_WOOL.get(container.getItem(i).getItem()));
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