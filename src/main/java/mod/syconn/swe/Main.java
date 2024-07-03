package mod.syconn.swe;

import mod.syconn.swe.client.ClientHandler;
import mod.syconn.swe.client.datagen.*;
import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.common.data.types.DimSettingsManager;
import mod.syconn.swe.common.data.types.OxygenProductionManager;
import mod.syconn.swe.init.*;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.util.config.Config;
import mod.syconn.swe.util.worldgen.dimension.MoonSpecialEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "swe";
    public Main() {
        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
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
