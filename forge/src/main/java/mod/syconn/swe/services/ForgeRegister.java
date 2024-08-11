package mod.syconn.swe.services;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.ForgeMod;
import mod.syconn.swe.helper.FluidTypes;
import mod.syconn.swe.platform.services.IRegistrar;
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
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fluids.FluidType;
import org.apache.commons.lang3.function.TriFunction;
import org.joml.Vector3f;

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

    public <T extends Fluid> Supplier<T> registerFluid(String id, Supplier<T> fluid) {
        return ForgeMod.FLUIDS.register(id, fluid);
    }

    public void registerFluidType(String id, ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay, int tint, Vector3f fog, String desc, boolean swim, boolean extinguish, boolean drown, PathType type, int lightLevel, int density, int viscosity, SoundEvent fill, SoundEvent empty, SoundEvent vaporize) {
        ForgeMod.FLUID_TYPES.register(id, () -> new FluidTypes(still, flowing, overlay, tint, fog, FluidType.Properties.create().descriptionId(desc).canSwim(swim).canExtinguish(extinguish).canDrown(drown).pathType(type).sound(SoundActions.BUCKET_FILL, fill).sound(SoundActions.BUCKET_EMPTY, empty).sound(SoundActions.FLUID_VAPORIZE, vaporize).lightLevel(lightLevel).density(density).viscosity(viscosity)));
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

    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return CreativeModeTab.builder();
    }
}
