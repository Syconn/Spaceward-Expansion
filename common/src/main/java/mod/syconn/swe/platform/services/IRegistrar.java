package mod.syconn.swe.platform.services;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.extra.IMenuData;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import org.apache.commons.lang3.function.TriFunction;
import org.joml.Vector3f;

import java.util.function.Supplier;

public interface IRegistrar {

    <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block);
    <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item);
    <T extends Fluid> Supplier<T> registerFluid(String id, Supplier<T> fluid);
    void registerFluidType(String id, ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay, int tint, Vector3f fog, String desc, boolean swim, boolean extinguish, boolean drown, PathType type, int lightLevel, int density, int viscosity, SoundEvent fill, SoundEvent empty, SoundEvent vaporize);
    <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> blockEntityType);
    <T extends AbstractContainerMenu, D extends IMenuData<D>> Supplier<MenuType<T>> registerMenuTypeWithData(String id, StreamCodec<RegistryFriendlyByteBuf, D> codec, TriFunction<Integer, Inventory, D, T> function);
    <T extends ArmorMaterial> Holder<T> registerArmorMaterial(String id, Supplier<T> armorMaterial);
    <T extends Recipe<?>> Supplier<RecipeSerializer<T>> registerRecipeSerializer(String id, Supplier<RecipeSerializer<T>> recipeSerializer);
    <T extends MapCodec<? extends Block>> Supplier<T> registerBlockCodec(String id, Supplier<T> blockCodec);
    <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String id, Supplier<T> tab);
    CreativeModeTab.Builder newCreativeTabBuilder();
    <T> Supplier<DataComponentType<T>> registerDataComponent(String id, Supplier<DataComponentType<T>> component);
}
