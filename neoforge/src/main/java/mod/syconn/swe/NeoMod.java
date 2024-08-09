package mod.syconn.swe;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.api.world.data.savedData.PipeNetworks;
import mod.syconn.swe.client.ClientHandler;
import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.common.dimensions.OxygenProductionManager;
import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.datagen.*;
import mod.syconn.swe.network.Channel;
import mod.syconn.swe.network.messages.ClientBoundUpdatePlanetSettings;
import mod.syconn.swe.services.NeoNetwork;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Collections;
import java.util.List;

@Mod(Constants.MOD_ID)
public class NeoMod {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Constants.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Constants.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Constants.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Constants.MOD_ID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_TYPE, Constants.MOD_ID);


    public NeoMod(IEventBus eventBus, ModContainer modContainer) {
        eventBus.register(NeoCommon.class);
        eventBus.addListener(NeoNetwork::onRegisterPayloadHandler);

        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        ITEMS.register(eventBus);
        ARMOR_MATERIALS.register(eventBus);
        DATA_COMPONENTS.register(eventBus);
        MENUS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
        BLOCK_TYPES.register(eventBus);

        if (FMLEnvironment.dist.isClient()) {
            NeoForge.EVENT_BUS.addListener(NeoClient::onPlayerRenderScreen);
            NeoForge.EVENT_BUS.addListener(NeoClient::renderBlockOutline);
        } else if (FMLEnvironment.dist.isDedicatedServer()) {
            NeoForge.EVENT_BUS.addListener(this::syncServerDataEvent);
        }

        NeoForge.EVENT_BUS.addListener(this::loadData);
        NeoForge.EVENT_BUS.addListener(NeoCommon::playerJoined);
        NeoForge.EVENT_BUS.addListener(NeoCommon::playerLeft);
        NeoForge.EVENT_BUS.addListener(NeoCommon::playerChangedDimension);
        NeoForge.EVENT_BUS.addListener(NeoCommon::playerTickEvent);
        NeoForge.EVENT_BUS.addListener(PipeNetworks::onTick);

        modContainer.registerConfig(ModConfig.Type.CLIENT, NeoConfig.CLIENT_CONFIG, "swe/swe-client.toml");
        modContainer.registerConfig(ModConfig.Type.COMMON, NeoConfig.COMMON_CONFIG, "swe/swe-common.toml");
        SpaceMod.init();
    }

    public void loadData(AddReloadListenerEvent e){
        e.addListener(new PlanetManager());
        e.addListener(new OxygenProductionManager());
    }

    public void syncServerDataEvent(OnDatapackSyncEvent event) {
        event.getRelevantPlayers().forEach(serverPlayer -> Channel.sendToPlayer(new ClientBoundUpdatePlanetSettings(List.copyOf(PlanetManager.getSettings())), serverPlayer));
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