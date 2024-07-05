package mod.syconn.swe;

import mod.syconn.swe.client.datagen.*;
import mod.syconn.swe.network.Channel;
import mod.syconn.swe.world.CommonHandler;
import mod.syconn.swe.world.dimensions.DimSettingsManager;
import mod.syconn.swe.world.dimensions.OxygenProductionManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "swe";
    public Main(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::loadData);
        modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(Registration::registerCapabilities);
        modEventBus.addListener(Channel::onRegisterPayloadHandler);

        Registration.ARMOR_MATERIALS.register(modEventBus);
        Registration.BLOCKS.register(modEventBus);
        Registration.ITEMS.register(modEventBus);
        Registration.BLOCK_ENTITIES.register(modEventBus);
        Registration.MENUS.register(modEventBus);
        Registration.RECIPE_SERIALIZERS.register(modEventBus);
        Registration.FLUID_TYPES.register(modEventBus);
        Registration.FLUIDS.register(modEventBus);
        Registration.TABS.register(modEventBus);
        Registration.ATTACHMENT_TYPES.register(modEventBus);
        Registration.COMPONENTS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CLIENT_CONFIG);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new CommonHandler());
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

    public void gatherData(GatherDataEvent event) {
        var fileHelper = event.getExistingFileHelper();
        var pack = event.getGenerator().getVanillaPack(true);
        pack.addProvider(LangGen::new);
        pack.addProvider(LootTableGen::new);
        pack.addProvider(pOutput -> new ItemModelGen(pOutput, fileHelper));
        pack.addProvider(pOutput -> new RecipeGen(pOutput, event.getLookupProvider()));
        pack.addProvider(pOutput -> new BlockStateGen(pOutput, fileHelper));
        pack.addProvider(pOutput -> new BlockTagsGen(pOutput, event.getLookupProvider(), fileHelper));
    }

    public void loadData(AddReloadListenerEvent e){
        e.addListener(new DimSettingsManager());
        e.addListener(new OxygenProductionManager());
    }

    public static ResourceLocation loc(String s) {
        return ResourceLocation.fromNamespaceAndPath(MODID, s);
    }
}
