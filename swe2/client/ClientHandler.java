package mod.syconn.swe2.client;

import mod.syconn.swe2.api.client.RenderUtil;
import mod.syconn.swe2.api.client.ber.FluidPipeBER;
import mod.syconn.swe2.api.client.debug.PipeNetworkRenderer;
import mod.syconn.swe2.api.client.loader.PipeModelLoader;
import mod.syconn.swe2.Config;
import mod.syconn.swe2.Main;
import mod.syconn.swe2.Registration;
import mod.syconn.swe2.client.model.ChuteModel;
import mod.syconn.swe2.client.model.ParachuteModel;
import mod.syconn.swe2.client.model.TankModel;
import mod.syconn.swe2.client.renders.ber.CanisterBER;
import mod.syconn.swe2.client.renders.ber.TankBER;
import mod.syconn.swe2.client.renders.effects.MoonSpecialEffects;
import mod.syconn.swe2.client.screen.CollectorScreen;
import mod.syconn.swe2.client.screen.DisperserScreen;
import mod.syconn.swe2.client.screen.TankScreen;
import mod.syconn.swe2.client.screen.gui.SpaceSuitOverlay;
import mod.syconn.swe2.items.Canister;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static mod.syconn.swe2.client.ClientHooks.addPlayerLayers;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ItemProperties.register(Registration.CANISTER.get(), Main.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        ItemProperties.register(Registration.AUTO_REFILL_CANISTER.get(), Main.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        ItemBlockRenderTypes.setRenderLayer(Registration.O2.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(Registration.O2_FLOWING.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        PipeModelLoader.register(event);
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(Registration.TANK_MENU.get(), TankScreen::new);
        event.register(Registration.DISPERSER_MENU.get(), DisperserScreen::new);
        event.register(Registration.COLLECTOR_MENU.get(), CollectorScreen::new);
    }

    @SubscribeEvent
    public static void coloredItems(RegisterColorHandlersEvent.Item event) {
        event.register((s, layer) -> layer == 0 ? DyedItemColor.getOrDefault(s, -1) : -1, Registration.PARACHUTE.get());
        event.register((s, layer) -> layer == 1  && s.getCapability(Capabilities.FluidHandler.ITEM) != null ? RenderUtil.getFluidColor(s.getCapability(Capabilities.FluidHandler.ITEM).getFluidInTank(0)) : -1, Registration.CANISTER.get(), Registration.AUTO_REFILL_CANISTER.get());
    }

    @SubscribeEvent
    public static void addRenderLayers(EntityRenderersEvent.AddLayers event) {
        ClientHooks.addPlayerLayers(event.getSkin(PlayerSkin.Model.WIDE), event.getEntityModels());
        ClientHooks.addPlayerLayers(event.getSkin(PlayerSkin.Model.SLIM), event.getEntityModels());
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
        event.registerLayerDefinition(ChuteModel.LAYER_LOCATION, ChuteModel::createBodyLayer);
        event.registerLayerDefinition(TankModel.LAYER_LOCATION, TankModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void renderOverlay(RegisterGuiLayersEvent event){
        event.registerAbove(VanillaGuiLayers.AIR_LEVEL, Main.loc("o2"), SpaceSuitOverlay.O2_OVERLAY);
    }

    public static void onPlayerRenderScreen(ContainerScreenEvent.Render.Background event) {
        ClientHooks.overridePlayerScreen(event.getGuiGraphics(), event.getContainerScreen());
    }

    @SubscribeEvent
    public static void entityRender(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(Registration.TANK.get(), TankBER::new);
        event.registerBlockEntityRenderer(Registration.FILLER.get(), CanisterBER::new);
        event.registerBlockEntityRenderer(Registration.PIPE.get(), FluidPipeBER::new);
    }

    @SubscribeEvent
    public static void dimensionEffects(RegisterDimensionSpecialEffectsEvent event){
        event.register(Main.loc("moon"), new MoonSpecialEffects());
    }

    public static void renderBlockOutline(RenderLevelStageEvent event) {
        if (Config.showPipeNetworks.get()) PipeNetworkRenderer.renderBlockOutline(event);
    }
}