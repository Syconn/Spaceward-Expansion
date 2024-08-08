package mod.syconn.swe;

import mod.syconn.swe.api.client.RenderUtil;
import mod.syconn.swe.client.model.ChuteModel;
import mod.syconn.swe.client.model.ParachuteModel;
import mod.syconn.swe.client.model.TankModel;
import mod.syconn.swe.client.renders.effects.MoonSpecialEffects;
import mod.syconn.swe.client.renders.entity.layer.SpaceSuitLayer;
import mod.syconn.swe.client.screen.gui.SpaceSuitOverlay;
import mod.syconn.swe.init.ItemRegister;
import mod.syconn.swe.items.Canister;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static mod.syconn.swe.items.Canister.getHandler;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClient {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ItemProperties.register(ItemRegister.CANISTER.get(), Constants.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        ItemProperties.register(ItemRegister.AUTO_REFILL_CANISTER.get(), Constants.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
    }

    @SubscribeEvent
    public static void coloredItems(RegisterColorHandlersEvent.Item event) {
        event.register((s, layer) -> layer == 0 ? DyedItemColor.getOrDefault(s, -1) : -1, ItemRegister.PARACHUTE.get());
        event.register((s, layer) -> layer == 1  && getHandler(s) != null ? RenderUtil.getFluidColor(getHandler(s).getFluidInTank().fluid()) : -1, ItemRegister.CANISTER.get(), ItemRegister.AUTO_REFILL_CANISTER.get());
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
        event.registerLayerDefinition(ChuteModel.LAYER_LOCATION, ChuteModel::createBodyLayer);
        event.registerLayerDefinition(TankModel.LAYER_LOCATION, TankModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void addRenderLayers(EntityRenderersEvent.AddLayers event) {
        addPlayerLayers(event.getPlayerSkin(PlayerSkin.Model.WIDE), event.getEntityModels());
        addPlayerLayers(event.getPlayerSkin(PlayerSkin.Model.SLIM), event.getEntityModels());
    }

    public static void addPlayerLayers(EntityRenderer<? extends Player> renderer, EntityModelSet s) {
        if(renderer instanceof PlayerRenderer playerRenderer) playerRenderer.addLayer(new SpaceSuitLayer<>(playerRenderer, s));
    }

    @SubscribeEvent
    public static void dimensionEffects(RegisterDimensionSpecialEffectsEvent event){
        event.register(Constants.loc("moon"), new MoonSpecialEffects());
    }
}
