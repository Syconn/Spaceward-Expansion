package mod.syconn.swe.client;

import mod.syconn.swe.client.model.ChuteModel;
import mod.syconn.swe.client.model.ParachuteModel;
import mod.syconn.swe.client.model.TankModel;

public class ClientHandler {

//    @SubscribeEvent TODO
//    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
//        PipeModelLoader.register(event);
//    }
//
//    @SubscribeEvent
//    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
//        event.register(Registration.TANK_MENU.get(), TankScreen::new);
//        event.register(Registration.DISPERSER_MENU.get(), DisperserScreen::new);
//        event.register(Registration.COLLECTOR_MENU.get(), CollectorScreen::new);
//    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
        event.registerLayerDefinition(ChuteModel.LAYER_LOCATION, ChuteModel::createBodyLayer);
        event.registerLayerDefinition(TankModel.LAYER_LOCATION, TankModel::createBodyLayer);
    }

// TODO   public static void onPlayerRenderScreen(ContainerScreenEvent.Render.Background event) {
//        ClientHooks.overridePlayerScreen(event.getGuiGraphics(), event.getContainerScreen());
//    }
//
//    @SubscribeEvent
//    public static void entityRender(EntityRenderersEvent.RegisterRenderers event){
//        event.registerBlockEntityRenderer(Registration.TANK.get(), TankBER::new);
//        event.registerBlockEntityRenderer(Registration.FILLER.get(), CanisterBER::new);
//        event.registerBlockEntityRenderer(Registration.PIPE.get(), FluidPipeBER::new);
//    }
//
//    public static void renderBlockOutline(RenderLevelStageEvent event) {
//        if (Config.showPipeNetworks.get()) PipeNetworkRenderer.renderBlockOutline(event);
//    }
}