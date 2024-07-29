package mod.syconn.swe.client;

import mod.syconn.api.client.ber.FluidPipeBER;
import mod.syconn.api.client.debug.PipeNetworkRenderer;
import mod.syconn.api.client.loader.PipeModelLoader;
import mod.syconn.swe.Main;
import mod.syconn.swe.Registration;
import mod.syconn.swe.client.model.*;
import mod.syconn.swe.client.renders.ber.CanisterBER;
import mod.syconn.swe.client.renders.ber.TankBER;
import mod.syconn.swe.client.screen.CollectorScreen;
import mod.syconn.swe.client.screen.DisperserScreen;
import mod.syconn.swe.client.screen.TankScreen;
import mod.syconn.swe.client.screen.gui.SpaceSuitOverlay;
import mod.syconn.swe.items.Canister;
import mod.syconn.swe.client.renders.effects.MoonSpecialEffects;
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

import static mod.syconn.swe.client.ClientHooks.addPlayerLayers;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ItemProperties.register(Registration.CANISTER.get(), Main.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        ItemProperties.register(Registration.AUTO_REFILL_CANISTER.get(), Main.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        ItemBlockRenderTypes.setRenderLayer(Registration.O2.get(), RenderType.translucent()); // TODO REPLACE IN JSON
        ItemBlockRenderTypes.setRenderLayer(Registration.O2_FLOWING.get(), RenderType.translucent()); // TODO REPLACE IN JSON
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
        addPlayerLayers(event.getSkin(PlayerSkin.Model.WIDE), event.getEntityModels());
        addPlayerLayers(event.getSkin(PlayerSkin.Model.SLIM), event.getEntityModels());
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
//      TODO EASY TOGGLE
//        PipeNetworkRenderer.renderBlockOutline(event);
    }
}