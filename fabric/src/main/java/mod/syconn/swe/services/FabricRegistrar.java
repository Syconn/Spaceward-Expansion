package mod.syconn.swe.services;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.Constants;
import mod.syconn.swe.platform.services.IRegistrar;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class FabricRegistrar implements IRegistrar {

    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> blockEntityType) {
        return registerSupplier(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
        return registerSupplier(BuiltInRegistries.BLOCK, id, block);
    }

    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        return registerSupplier(BuiltInRegistries.ITEM, id, item);
    }

    public <T extends ArmorMaterial> Holder<T> registerArmorMaterial(String id, Supplier<T> armorMaterial) {
        return registerHolder(BuiltInRegistries.ARMOR_MATERIAL, id, armorMaterial);
    }

    public <T extends SoundEvent> Supplier<T> registerSound(String id, Supplier<T> sound) {
        return registerSupplier(BuiltInRegistries.SOUND_EVENT, id, sound);
    }

    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String id, Supplier<T> tab) {
        return registerSupplier(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab);
    }

    public <T> Supplier<DataComponentType<T>> registerDataComponent(String id, Supplier<DataComponentType<T>> component) {
        return registerSupplier(BuiltInRegistries.DATA_COMPONENT_TYPE, id, component);
    }

    public <T extends Fluid> Supplier<T> registerFluid(String id, Supplier<T> fluid) {
        return registerSupplier(BuiltInRegistries.FLUID, id, fluid);
    }

    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(String id, Supplier<MenuType<T>> menuType) {
        return registerSupplier(BuiltInRegistries.MENU, id, menuType);
    }

    public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> registerRecipeSerializer(String id, Supplier<RecipeSerializer<T>> recipeSerializer) {
        return registerSupplier(BuiltInRegistries.RECIPE_SERIALIZER, id, recipeSerializer);
    }

    public <T extends MapCodec<? extends Block>> Supplier<T> registerBlockCodec(String id, Supplier<T> blockCodec) {
        return registerSupplier(BuiltInRegistries.BLOCK_TYPE, id, blockCodec);
    }

    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return FabricItemGroup.builder();
    }

    public void registerFluidType(String id, ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay, int tint, Vector3f fog, String desc, boolean swim, boolean extinguish, boolean drown, PathType type, int lightLevel, int density, int viscosity, SoundEvent fill, SoundEvent empty, SoundEvent vaporize) {}

    private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(R registry, String id, Supplier<T> object) {
        final T registeredObject = Registry.register((Registry<T>)registry,  ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), object.get());
        return () -> registeredObject;
    }

    private static <T, R extends Registry<? super T>> Holder<T> registerHolder(R registry, String id, Supplier<T> object) {
        return Registry.registerForHolder((Registry<T>) registry,  ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), object.get());
    }
}
