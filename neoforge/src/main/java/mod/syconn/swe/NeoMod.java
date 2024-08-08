package mod.syconn.swe;

import mod.syconn.swe.datagen.*;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collections;
import java.util.List;

@Mod(Constants.MOD_ID)
public class NeoMod {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, Constants.MOD_ID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

    public NeoMod(IEventBus eventBus) {
        eventBus.addListener(NeoCommon::registerCapabilities);

        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        ENTITIES.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        ITEMS.register(eventBus);
        ARMOR_MATERIALS.register(eventBus);
        DATA_COMPONENTS.register(eventBus);

        SpaceMod.init();
    }

    public void gatherData(GatherDataEvent event) {
        var fileHelper = event.getExistingFileHelper();
        var pack = event.getGenerator().getVanillaPack(true);
        BlockTagsGen blockTags = new BlockTagsGen(event.getGenerator().getPackOutput(), event.getLookupProvider(), fileHelper);
        pack.addProvider(LangGen::new);
        pack.addProvider(pOutput -> new RecipeGen(pOutput, event.getLookupProvider()));
        pack.addProvider(pOutput -> new ItemModelGen(pOutput, fileHelper));
        pack.addProvider(pOutput -> new BlockModelGen(pOutput, fileHelper));
        pack.addProvider(pOutput -> blockTags);
        pack.addProvider(pOutput -> new ItemTagsGen(pOutput, event.getLookupProvider(), blockTags.contentsGetter(), fileHelper));
        pack.addProvider(pOutput -> new FluidTagsGen(pOutput, event.getLookupProvider(), fileHelper));
        pack.addProvider(pOutput -> new LootTableProvider(pOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)), event.getLookupProvider()));
    }
}