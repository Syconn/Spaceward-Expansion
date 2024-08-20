package mod.syconn.swe;

import mod.syconn.swe.client.ClientHooks;
import mod.syconn.swe.client.model.ChuteModel;
import mod.syconn.swe.client.model.ParachuteModel;
import mod.syconn.swe.client.model.TankModel;
import mod.syconn.swe.client.renders.ber.CanisterBER;
import mod.syconn.swe.client.renders.ber.FluidPipeBER;
import mod.syconn.swe.client.renders.ber.TankBER;
import mod.syconn.swe.client.renders.debug.PipeDebugRenderer;
import mod.syconn.swe.client.renders.effects.MoonSpecialEffects;
import mod.syconn.swe.client.renders.entity.layer.SpaceSuitLayer;
import mod.syconn.swe.client.screen.CollectorScreen;
import mod.syconn.swe.client.screen.DisperserScreen;
import mod.syconn.swe.client.screen.TankScreen;
import mod.syconn.swe.client.screen.gui.SpaceSuitOverlay;
import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.extra.core.Events;
import mod.syconn.swe.extra.util.RenderUtil;
import mod.syconn.swe.init.BlockEntityRegister;
import mod.syconn.swe.init.FluidRegister;
import mod.syconn.swe.init.ItemRegister;
import mod.syconn.swe.init.Menus;
import mod.syconn.swe.items.Canister;
import mod.syconn.swe.model.loader.PipeModelLoader;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static mod.syconn.swe.items.Canister.getHandler;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NeoClient {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ItemProperties.register(ItemRegister.CANISTER.get(), Constants.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        ItemProperties.register(ItemRegister.AUTO_REFILL_CANISTER.get(), Constants.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        ItemBlockRenderTypes.setRenderLayer(FluidRegister.O2.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegister.O2_FLOWING.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void coloredItems(RegisterColorHandlersEvent.Item event) {
        event.register((s, layer) -> layer == 0 ? DyedItemColor.getOrDefault(s, -1) : -1, ItemRegister.PARACHUTE.get());
        event.register((s, layer) -> layer == 1  && getHandler(s) != null ? RenderUtil.getFluidColor(getHandler(s).getFluidHolder().getFluid()) : -1, ItemRegister.CANISTER.get(), ItemRegister.AUTO_REFILL_CANISTER.get());
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(NeoRegistration.O2_FLUID_TYPE.get().getExtension(), NeoRegistration.O2_FLUID_TYPE.get());
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
        event.registerLayerDefinition(ChuteModel.LAYER_LOCATION, ChuteModel::createBodyLayer);
        event.registerLayerDefinition(TankModel.LAYER_LOCATION, TankModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void dimensionEffects(RegisterDimensionSpecialEffectsEvent event){
        event.register(Constants.loc("moon"), new MoonSpecialEffects());
    }

    @SubscribeEvent
    public static void registerClientLoaders(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new PlanetManager());
    }

    @SubscribeEvent
    public static void renderOverlay(RegisterGuiLayersEvent event){
        event.registerAbove(VanillaGuiLayers.AIR_LEVEL, Constants.loc("o2"), SpaceSuitOverlay.O2_OVERLAY);
    }

    @SubscribeEvent
    public static void entityRender(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntityRegister.TANK.get(), TankBER::new);
        event.registerBlockEntityRenderer(BlockEntityRegister.FILLER.get(), CanisterBER::new);
        event.registerBlockEntityRenderer(BlockEntityRegister.PIPE.get(), FluidPipeBER::new);
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(Menus.TANK_MENU.get(), TankScreen::new);
        event.register(Menus.DISPERSER_MENU.get(), DisperserScreen::new);
        event.register(Menus.COLLECTOR_MENU.get(), CollectorScreen::new);
    }

    @SubscribeEvent
    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        PipeModelLoader.register(event);
    }

    public static void onPlayerRenderScreen(ContainerScreenEvent.Render.Background event) {
        ClientHooks.overrideAbstractScreen(event.getGuiGraphics(), event.getContainerScreen(), event.getContainerScreen().getGuiLeft(), event.getContainerScreen().getGuiTop());
    }

    @SubscribeEvent
    public static void addRenderLayers(EntityRenderersEvent.AddLayers event) {
        addPlayerLayers(event.getSkin(PlayerSkin.Model.WIDE), event.getEntityModels());
        addPlayerLayers(event.getSkin(PlayerSkin.Model.SLIM), event.getEntityModels());
    }

    public static void addPlayerLayers(EntityRenderer<? extends Player> renderer, EntityModelSet s) {
        if(renderer instanceof PlayerRenderer playerRenderer) playerRenderer.addLayer(new SpaceSuitLayer<>(playerRenderer, s));
    }

    public static void renderBlockOutline(RenderLevelStageEvent event) {
        // TODO CONFIG IMPLEMENTATION
//        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) PipeDebugRenderer.renderBlockOutline(new Events.LevelRenderStage(event.getPoseStack(), event.getModelViewMatrix(), event.getProjectionMatrix()));
    }
}
