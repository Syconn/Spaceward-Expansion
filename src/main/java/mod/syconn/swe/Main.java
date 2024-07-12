package mod.syconn.swe;

import mod.syconn.swe.client.ClientHandler;
import mod.syconn.swe.client.datagen.*;
import mod.syconn.swe.network.Channel;
import mod.syconn.swe.world.CommonHandler;
import mod.syconn.swe.world.dimensions.DimSettingsManager;
import mod.syconn.swe.world.dimensions.OxygenProductionManager;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.Collections;
import java.util.List;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "swe";

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(CommonHandler::init);
        modEventBus.addListener(ClientHandler::init);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(Registration::registerCapabilities);
        modEventBus.addListener(Channel::onRegisterPayloadHandler);

        Registration.ARMOR_MATERIALS.register(modEventBus);
        Registration.BLOCKS.register(modEventBus);
        Registration.FLUID_TYPES.register(modEventBus);
        Registration.FLUIDS.register(modEventBus);
        Registration.ATTACHMENT_TYPES.register(modEventBus);
        Registration.ITEMS.register(modEventBus);
        Registration.BLOCK_ENTITIES.register(modEventBus);
        Registration.MENUS.register(modEventBus);
        Registration.RECIPE_SERIALIZERS.register(modEventBus);
        Registration.TABS.register(modEventBus);
        Registration.COMPONENTS.register(modEventBus);

        NeoForge.EVENT_BUS.addListener(this::loadData);
        NeoForge.EVENT_BUS.addListener(CommonHandler::playerTickEvent);
        NeoForge.EVENT_BUS.addListener(ClientHandler::onPlayerRenderScreen);

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG, "swe/swe-client.toml");
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG, "swe/swe-common.toml");
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
        pack.addProvider(pOutput -> new LootTableProvider(pOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)), event.getLookupProvider()));
    }

    public void loadData(AddReloadListenerEvent e){
        e.addListener(new DimSettingsManager());
        e.addListener(new OxygenProductionManager());
    }

    public static ResourceLocation loc(String s) {
        return ResourceLocation.fromNamespaceAndPath(MODID, s);
    }
}
