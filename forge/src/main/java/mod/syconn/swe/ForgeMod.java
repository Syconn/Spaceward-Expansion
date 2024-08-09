package mod.syconn.swe;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.common.dimensions.OxygenProductionManager;
import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.network.Channel;
import mod.syconn.swe.network.messages.ClientBoundUpdatePlanetSettings;
import mod.syconn.swe.services.ForgeNetwork;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod(Constants.MOD_ID)
public class ForgeMod {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, Constants.MOD_ID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.FLUID_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Constants.MOD_ID);
    public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(Registries.BLOCK_TYPE, Constants.MOD_ID);

    public ForgeMod() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(ForgeCommon.class);
        eventBus.addListener(ForgeNetwork::setupNetwork);

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
            MinecraftForge.EVENT_BUS.addListener(ForgeClient::onPlayerRenderScreen);
            MinecraftForge.EVENT_BUS.addListener(ForgeClient::renderBlockOutline);
        } else if (FMLEnvironment.dist.isDedicatedServer()) {
            MinecraftForge.EVENT_BUS.addListener(this::syncServerDataEvent);
        }

        MinecraftForge.EVENT_BUS.addListener(this::loadData);
        MinecraftForge.EVENT_BUS.addListener(ForgeCommon::playerJoined);
        MinecraftForge.EVENT_BUS.addListener(ForgeCommon::playerLeft);
        MinecraftForge.EVENT_BUS.addListener(ForgeCommon::playerChangedDimension);
        MinecraftForge.EVENT_BUS.addListener(ForgeCommon::playerTickEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeConfig.COMMON_CONFIG, "swe/swe-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeConfig.CLIENT_CONFIG, "swe/swe-common.toml");
        SpaceMod.init();
    }

    public void loadData(AddReloadListenerEvent e){
        e.addListener(new PlanetManager());
        e.addListener(new OxygenProductionManager());
    }

    public void syncServerDataEvent(OnDatapackSyncEvent event) {
        event.getPlayers().forEach(serverPlayer -> Channel.sendToPlayer(new ClientBoundUpdatePlanetSettings(List.copyOf(PlanetManager.getSettings())), serverPlayer));
    }
}