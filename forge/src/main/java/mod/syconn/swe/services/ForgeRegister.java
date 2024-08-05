package mod.syconn.swe.services;

import mod.syconn.swe.ForgeMod;
import mod.syconn.swe.platform.services.IRegistrar;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.Supplier;

public class ForgeRegister implements IRegistrar {

    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String id, Supplier<BlockEntityType<T>> blockEntityType) {
        return ForgeMod.BLOCK_ENTITIES.register(id, blockEntityType);
    }

    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
        return ForgeMod.BLOCKS.register(id, block);
    }

    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, Supplier<EntityType<T>> entity) {
        return ForgeMod.ENTITIES.register(id, entity);
    }

    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        return ForgeMod.ITEMS.register(id, item);
    }

    public <T extends ArmorMaterial> Supplier<T> registerArmorMaterial(String id, Supplier<T> armorMaterial) {
        return ForgeMod.ARMOR_MATERIALS.register(id, armorMaterial);
    }

    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String id, Supplier<T> tab) {
        return ForgeMod.CREATIVE_TABS.register(id, tab);
    }

    public <T> Supplier<DataComponentType<T>> registerDataComponent(String id, Supplier<DataComponentType<T>> component) {
        return ForgeMod.DATA_COMPONENTS.register(id, component);
    }

    public <E extends Mob> Supplier<SpawnEggItem> makeSpawnEggFor(Supplier<EntityType<E>> entityType, int primaryEggColour, int secondaryEggColour, Item.Properties itemProperties) {
        return () -> new ForgeSpawnEggItem(entityType, primaryEggColour, secondaryEggColour, itemProperties);
    }

    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return CreativeModeTab.builder();
    }
}
