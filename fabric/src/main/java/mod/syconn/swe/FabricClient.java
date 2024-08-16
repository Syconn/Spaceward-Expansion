package mod.syconn.swe;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swe.blocks.fluids.OxygenFlowingFluid;
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
import mod.syconn.swe.events.RenderEvents;
import mod.syconn.swe.extra.core.Events;
import mod.syconn.swe.extra.util.RenderUtil;
import mod.syconn.swe.init.BlockEntityRegister;
import mod.syconn.swe.init.FluidRegister;
import mod.syconn.swe.init.ItemRegister;
import mod.syconn.swe.init.Menus;
import mod.syconn.swe.items.Canister;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.reloaders.FabricPlanetManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.component.DyedItemColor;

import static mod.syconn.swe.items.Canister.getHandler;

public class FabricClient implements ClientModInitializer {

    public void onInitializeClient() {
        Network.registerMessages();
        Network.C2SPayloads();
        ModelLoadingPlugin.register(new ModelLoader());

        MenuScreens.register(Menus.TANK_MENU.get(), TankScreen::new);
        MenuScreens.register(Menus.DISPERSER_MENU.get(), DisperserScreen::new);
        MenuScreens.register(Menus.COLLECTOR_MENU.get(), CollectorScreen::new);
        BlockEntityRenderers.register(BlockEntityRegister.TANK.get(), TankBER::new);
        BlockEntityRenderers.register(BlockEntityRegister.FILLER.get(), CanisterBER::new);
        BlockEntityRenderers.register(BlockEntityRegister.PIPE.get(), FluidPipeBER::new);
        ItemProperties.register(ItemRegister.CANISTER.get(), Constants.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        ItemProperties.register(ItemRegister.AUTO_REFILL_CANISTER.get(), Constants.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        EntityModelLayerRegistry.registerModelLayer(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ChuteModel.LAYER_LOCATION, ChuteModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(TankModel.LAYER_LOCATION, TankModel::createBodyLayer);
        DimensionRenderingRegistry.registerDimensionEffects(Constants.loc("moon"), new MoonSpecialEffects());
        ColorProviderRegistry.ITEM.register((s, layer) -> layer == 0 ? DyedItemColor.getOrDefault(s, -1) : -1, ItemRegister.PARACHUTE.get());
        ColorProviderRegistry.ITEM.register((s, layer) -> layer == 1  && getHandler(s) != null ? RenderUtil.getFluidColor(getHandler(s).getFluid().getFluid()) : -1, ItemRegister.CANISTER.get(), ItemRegister.AUTO_REFILL_CANISTER.get());
        FluidRenderHandlerRegistry.INSTANCE.register(FluidRegister.O2.get(), FluidRegister.O2_FLOWING.get(), new SimpleFluidRenderHandler(OxygenFlowingFluid.O2_STILL_RL, OxygenFlowingFluid.O2_FLOWING_RL, OxygenFlowingFluid.O2_OVERLAY_RL, -1));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), FluidRegister.O2.get(), FluidRegister.O2_FLOWING.get());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricPlanetManager());

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if(entityRenderer instanceof PlayerRenderer renderer) registrationHelper.register(new SpaceSuitLayer<>(renderer, context.getModelSet()));
        });
        HudRenderCallback.EVENT.register(SpaceSuitOverlay.O2_OVERLAY::render);
        RenderEvents.RENDER_STAGE_EVENT.register(((type, levelRenderer, modelViewMatrix, projectionMatrix, renderTick, partialTick, camera, frustum) -> {
            if (type == RenderType.tripwire()) PipeDebugRenderer.renderBlockOutline(new Events.LevelRenderStage(new PoseStack(), modelViewMatrix, projectionMatrix));
        }));
    }

    @Environment(EnvType.CLIENT)
    private class ModelLoader implements ModelLoadingPlugin {

        public void onInitializeModelLoader(Context pluginContext) {
            pluginContext.modifyModelOnLoad().register((original, context) -> {
                if(context.resourceId().equals(Constants.loc("pipe"))) return new PipeModelLoader();
                return original;
            });
        }
    }
}
