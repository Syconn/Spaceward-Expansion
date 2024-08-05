package mod.syconn.swe.services;

import mod.syconn.swe.NeoMod;
import mod.syconn.swe.platform.services.IRegistrar;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class NeoRegister implements IRegistrar {

    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> blockEntityType) {
        return NeoMod.BLOCK_ENTITIES.register(id, blockEntityType);
    }

    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
        return NeoMod.BLOCKS.register(id, block);
    }

    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, Supplier<EntityType<T>> entity) {
        return NeoMod.ENTITIES.register(id, entity);
    }

    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        return NeoMod.ITEMS.register(id, item);
    }

    public <T extends ArmorMaterial> Supplier<T> registerArmorMaterial(String id, Supplier<T> armorMaterial) {
        return NeoMod.ARMOR_MATERIALS.register(id, armorMaterial);
    }

    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String id, Supplier<T> tab) {
        return NeoMod.CREATIVE_TABS.register(id, tab);
    }

    public <T> Supplier<DataComponentType<T>> registerDataComponent(String id, Supplier<DataComponentType<T>> component) {
        return NeoMod.DATA_COMPONENTS.register(id, component);
    }

    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return CreativeModeTab.builder();
    }
}