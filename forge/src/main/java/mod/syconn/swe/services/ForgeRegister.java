package mod.syconn.swe.services;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.ForgeMod;
import mod.syconn.swe.ForgeRegistration;
import mod.syconn.swe.blocks.fluids.OxygenFlowingFluid;
import mod.syconn.swe.extra.core.IMenuData;
import mod.syconn.swe.extra.platform.services.IRegistrar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.extensions.IForgeMenuType;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ForgeRegister implements IRegistrar {

    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> blockEntityType) {
        return ForgeMod.BLOCK_ENTITIES.register(id, blockEntityType);
    }

    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
        return ForgeMod.BLOCKS.register(id, block);
    }

    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        return ForgeMod.ITEMS.register(id, item);
    }

    public <T extends ArmorMaterial> Holder<T> registerArmorMaterial(String id, Supplier<T> armorMaterial) {
        return ForgeMod.ARMOR_MATERIALS.register(id, armorMaterial).getHolder().orElseThrow();
    }

    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String id, Supplier<T> tab) {
        return ForgeMod.CREATIVE_TABS.register(id, tab);
    }

    public <T> Supplier<DataComponentType<T>> registerDataComponent(String id, Supplier<DataComponentType<T>> component) {
        return ForgeMod.DATA_COMPONENTS.register(id, component);
    }

    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String id, BiFunction<BlockPos, BlockState, T> function, Supplier<Block> blockSupplier) {
        return ForgeMod.BLOCK_ENTITIES.register(id, () -> BlockEntityType.Builder.of(function::apply, blockSupplier.get()).build(null));
    }

    public <T extends BlockEntity> BiFunction<BlockPos, BlockState, T> createBEType(BiFunction<BlockPos, BlockState, T> function) {
        return function;
    }

    public <T extends AbstractContainerMenu, D extends IMenuData<D>> Supplier<MenuType<T>> registerMenuTypeWithData(String id, StreamCodec<RegistryFriendlyByteBuf, D> codec, TriFunction<Integer, Inventory, D, T> function) {
        return ForgeMod.MENUS.register(id, () -> IForgeMenuType.create((windowId, inv, data) -> {
            RegistryFriendlyByteBuf buf = RegistryFriendlyByteBuf.decorator(inv.player.registryAccess()).apply(data);
            return function.apply(windowId, inv, codec.decode(buf));
        }));
    }

    public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> registerRecipeSerializer(String id, Supplier<RecipeSerializer<T>> recipeSerializer) {
        return ForgeMod.RECIPE_SERIALIZERS.register(id, recipeSerializer);
    }

    public <T extends MapCodec<? extends Block>> Supplier<T> registerBlockCodec(String id, Supplier<T> blockCodec) {
        return ForgeMod.BLOCK_TYPES.register(id, blockCodec);
    }

    @SuppressWarnings("unchecked")
    public <T extends Fluid> Supplier<T> registerFluid(String id, Class<T> fluid) {
        if (fluid.isInstance(OxygenFlowingFluid.Source.class)) return (Supplier<T>) ForgeRegistration.O2;
        return (Supplier<T>) ForgeRegistration.O2_FLOWING;
    }

    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return CreativeModeTab.builder();
    }
}
