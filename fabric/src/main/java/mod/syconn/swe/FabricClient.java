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
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.component.DyedItemColor;

import static mod.syconn.swe.items.Canister.getHandler;

public class FabricClient implements ClientModInitializer {

    public void onInitializeClient() {
        ItemProperties.register(ItemRegister.CANISTER.get(), Constants.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        ItemProperties.register(ItemRegister.AUTO_REFILL_CANISTER.get(), Constants.loc("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if(entityRenderer instanceof PlayerRenderer renderer) registrationHelper.register(new SpaceSuitLayer<>(renderer, context.getModelSet()));
        });
        HudRenderCallback.EVENT.register(SpaceSuitOverlay.O2_OVERLAY::render);
        EntityModelLayerRegistry.registerModelLayer(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(ChuteModel.LAYER_LOCATION, ChuteModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(TankModel.LAYER_LOCATION, TankModel::createBodyLayer);
        DimensionRenderingRegistry.registerDimensionEffects(Constants.loc("moon"), new MoonSpecialEffects());
        ColorProviderRegistry.ITEM.register((s, layer) -> layer == 0 ? DyedItemColor.getOrDefault(s, -1) : -1, ItemRegister.PARACHUTE.get());
        ColorProviderRegistry.ITEM.register((s, layer) -> layer == 1  && getHandler(s) != null ? RenderUtil.getFluidColor(getHandler(s).getFluidInTank().fluid()) : -1, ItemRegister.CANISTER.get(), ItemRegister.AUTO_REFILL_CANISTER.get());
    }
}
