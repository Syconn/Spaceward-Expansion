package syconn.swe;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import syconn.swe.client.ClientHandler;
import syconn.swe.client.datagen.*;
import syconn.swe.common.CommonHandler;
import syconn.swe.common.data.types.DimSettingsManager;
import syconn.swe.common.data.types.OxygenProductionManager;
import syconn.swe.util.config.Config;
import syconn.swe.init.*;
import syconn.swe.network.Network;
import syconn.swe.util.worldgen.dimension.MoonSpecialEffects;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "swe";
    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(this::createTab);
            modEventBus.addListener(ClientHandler::onRegisterLayers);
            modEventBus.addListener(ClientHandler::addLayers);
            modEventBus.addListener(ClientHandler::renderOverlay);
            modEventBus.addListener(ClientHandler::addLayers);
            modEventBus.addListener(ClientHandler::coloredBlocks);
            modEventBus.addListener(ClientHandler::coloredItems);
            modEventBus.addListener(ClientHandler::entityRender);
        });
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::dataGenerator);
        modEventBus.addListener(this::dimensionEffects);

        ModContainers.REGISTER.register(modEventBus);
        ModBlockEntity.REGISTER.register(modEventBus);
        ModInit.BLOCKS.register(modEventBus);
        ModInit.ITEMS.register(modEventBus);
        ModRecipeSerializer.REGISTER.register(modEventBus);
        ModFluids.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);

        FileUtils.getOrCreateDirectory(FMLPaths.CONFIGDIR.get().resolve(MODID), MODID);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG, "swe/sweclient.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Network.init();
        MinecraftForge.EVENT_BUS.register(new CommonHandler());
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientHandler());
    }

    public void dataGenerator(GatherDataEvent e){
        e.getGenerator().addProvider(e.includeClient(), new ItemModelGen(e.getGenerator().getPackOutput(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new BlockModelGen(e.getGenerator().getPackOutput(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new LangGen(e.getGenerator().getPackOutput()));
        e.getGenerator().addProvider(e.includeServer(), new RecipeGen(e.getGenerator().getPackOutput()));
        e.getGenerator().addProvider(e.includeServer(), new ItemTagsGen(e.getGenerator().getPackOutput(), e.getLookupProvider(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeServer(), new BlockTagsGen(e.getGenerator().getPackOutput(), e.getLookupProvider(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeServer(), new LootTableGen(e.getGenerator().getPackOutput()));
    }

    public void dimensionEffects(RegisterDimensionSpecialEffectsEvent e){
        e.register(new ResourceLocation(MODID, "moon"), new MoonSpecialEffects());
    }
    public void createTab(CreativeModeTabEvent.Register e){
        e.registerCreativeModeTab(new ResourceLocation(MODID, "space"), builder -> builder.noScrollBar().title(Component.translatable("itemGroup.space")).icon(() -> new ItemStack(ModInit.SPACE_HELMET.get())).displayItems((a, p) -> ModInit.addItems(p)).build());
    }

    @SubscribeEvent
    public void loadData(AddReloadListenerEvent e){
        e.addListener(new DimSettingsManager());
        e.addListener(new OxygenProductionManager());
    }
}
