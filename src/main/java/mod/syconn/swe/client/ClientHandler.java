package mod.syconn.swe.client;

import mod.syconn.swe.client.gui.SpaceSuitOverlay;
import mod.syconn.swe.client.model.*;
import mod.syconn.swe.client.renders.ber.CanisterBER;
import mod.syconn.swe.client.renders.ber.PipeBER;
import mod.syconn.swe.client.renders.ber.TankBER;
import mod.syconn.swe.client.renders.entity.layer.SpaceSuitLayer;
import mod.syconn.swe.client.screen.*;
import mod.syconn.swe.items.Canister;
import mod.syconn.swe.util.ColorUtil;
import mod.syconn.swe.util.Dyeable;
import mod.syconn.swe.util.worldgen.dimension.MoonSpecialEffects;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import org.jetbrains.annotations.Nullable;

import static mod.syconn.swe.Main.MODID;

@EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientHandler {

    public ClientHandler() {
        registerProperties();
        MenuScreens.register(ModContainers.TANK_MENU.get(), TankScreen::new);
        MenuScreens.register(ModContainers.PIPE_MENU.get(), PipeScreen::new);
        MenuScreens.register(ModContainers.DISPERSER_MENU.get(), DisperserScreen::new);
        MenuScreens.register(ModContainers.COLLECTOR_MENU.get(), CollectorScreen::new);
        ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_O2_FLUID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_O2_FLUID.get(), RenderType.translucent());
    }

    public static void registerProperties(){
        ItemProperties.register(ModInit.CANISTER.get(), new ResourceLocation(MODID, "stage"), new ItemPropertyFunction() {
            public float call(ItemStack stack, @Nullable ClientLevel p_174677_, @Nullable LivingEntity p_174678_, int p_174679_) { return Canister.getDisplayValue(stack); }
        });
        ItemProperties.register(ModInit.AUTO_REFILL_CANISTER.get(), new ResourceLocation(MODID, "stage"), new ItemPropertyFunction() {
            public float call(ItemStack stack, @Nullable ClientLevel p_174677_, @Nullable LivingEntity p_174678_, int p_174679_) { return Canister.getDisplayValue(stack); }
        });
    }

    @SubscribeEvent
    public static void coloredItems(RegisterColorHandlersEvent.Item e) {
        e.register((s, layer) -> layer == 0 ? Dyeable.getColor(s) : -1, ModInit.PARACHUTE.get());
        e.register((s, layer) -> layer == 1 ? ColorUtil.getClosetColor(s.getBarColor()).getMaterialColor().col : -1, ModInit.CANISTER.get(), ModInit.AUTO_REFILL_CANISTER.get());
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers e) {
        addPlayerLayers(e.getSkin("default"), e.getEntityModels());
        addPlayerLayers(e.getSkin("slim"), e.getEntityModels());
    }

    private static void addPlayerLayers(LivingEntityRenderer<?, ?> renderer, EntityModelSet s) {
        if(renderer instanceof PlayerRenderer playerRenderer) playerRenderer.addLayer(new SpaceSuitLayer<>(playerRenderer, s));
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
        event.registerLayerDefinition(ChuteModel.LAYER_LOCATION, ChuteModel::createBodyLayer);
        event.registerLayerDefinition(TankModel.LAYER_LOCATION, TankModel::createBodyLayer);
        event.registerLayerDefinition(FluidPipeModel.LAYER_LOCATION, FluidPipeModel::createBodyLayer);
        event.registerLayerDefinition(FluidModel.LAYER_LOCATION, FluidModel::createBodyLayer);
        event.registerLayerDefinition(FluidInPipeModel.LAYER_LOCATION, FluidInPipeModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void renderOverlay(RegisterGuiOverlaysEvent e){
        e.registerAbove(VanillaGuiOverlay.AIR_LEVEL.id(), "o2", SpaceSuitOverlay.O2_OVERLAY);
    }

    @SubscribeEvent
    public static void onPlayerRenderScreen(ContainerScreenEvent.Render.Background event) {
        RenderUtil.overridePlayerScreen(event.getPoseStack(), event.getContainerScreen());
    }

    @SubscribeEvent
    public static void entityRender(EntityRenderersEvent.RegisterRenderers e){
        e.registerBlockEntityRenderer(ModBlockEntity.PIPE.get(), PipeBER::new);
        e.registerBlockEntityRenderer(ModBlockEntity.TANK.get(), TankBER::new);
        e.registerBlockEntityRenderer(ModBlockEntity.FILLER.get(), CanisterBER::new);
    }

    @SubscribeEvent
    public static void dimensionEffects(RegisterDimensionSpecialEffectsEvent e){
        e.register(new ResourceLocation(MODID, "moon"), new MoonSpecialEffects());
    }
}