package mod.syconn.swe;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import mod.syconn.swe.client.model.ChuteModel;
import mod.syconn.swe.client.model.ParachuteModel;
import mod.syconn.swe.client.model.TankModel;
import mod.syconn.swe.client.screen.overlay.SpaceSuitOverlay;
import mod.syconn.swe.common.CommonHandler;
import mod.syconn.swe.common.items.Canister;
import mod.syconn.swe.common.items.Parachute;
import mod.syconn.swe.core.*;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.server.reloaders.OxygenProductionManager;
import mod.syconn.swe.server.reloaders.PlanetManager;
import mod.syconn.swe.server.savedData.PipeNetworks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.DyeableLeatherItem;

public class SpaceMod {

    public static void init() {
        ModFluids.FLUIDS.register();
        ModItems.ITEMS.register();
        ModBlocks.BLOCKS.register();
        ModBlockEntities.BLOCK_ENTITIES.register();
        ModItems.TABS.register();
        ModMenus.MENUS.register();
        ModRecipes.RECIPE_SERIALIZERS.register();
        ModAttributes.ATTRIBUTES.register();

        CreativeTabRegistry.modify(ModItems.TAB, ModItems::addCreative);

        TickEvent.SERVER_LEVEL_PRE.register(PipeNetworks::tickNetworks);
        TickEvent.PLAYER_PRE.register(CommonHandler::playerTickEvent);
        PlayerEvent.PLAYER_JOIN.register(CommonHandler::playerJoined);
        PlayerEvent.PLAYER_QUIT.register(CommonHandler::playerQuit);

        EnvExecutor.runInEnv(Env.CLIENT, () -> Client::init);
        EnvExecutor.runInEnv(Env.SERVER, () -> Server::init);
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        @Environment(EnvType.CLIENT)
        public static void init() {
            ClientLifecycleEvent.CLIENT_SETUP.register(ModMenus::registerScreens);
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Constants.withId("animation"), 42, Client::registerPlayerAnimation);
            ClientGuiEvent.RENDER_HUD.register(SpaceSuitOverlay::renderOverlay);

            ItemPropertiesRegistry.register(ModItems.CANISTER.get(), Constants.withId("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
            ItemPropertiesRegistry.register(ModItems.AUTO_REFILL_CANISTER.get(), Constants.withId("stage"), (pStack, pLevel, pEntity, pSeed) -> Canister.getDisplayValue(pStack));
            RenderTypeRegistry.register(RenderType.translucent(), ModFluids.O2.get(), ModFluids.O2_FLOWING.get());
            ColorHandlerRegistry.registerItemColors((s, layer) -> layer == 0 ? ((DyeableLeatherItem) s.getItem()).getColor(s) : -1, ModItems.PARACHUTE.get());
//  TODO          ColorHandlerRegistry.registerItemColors((s, layer) -> layer == 1  && s.getCapability(Capabilities.FluidHandler.ITEM) != null ? RenderUtil.getFluidColor(s.getCapability(Capabilities.FluidHandler.ITEM).getFluidInTank(0)) : -1, Registration.CANISTER.get(), Registration.AUTO_REFILL_CANISTER.get());

            Network.initC2S();
        }

        private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
            return new ModifierLayer<>();
        }
    }

    @Environment(EnvType.SERVER)
    public static class Server {

        @Environment(EnvType.SERVER)
        public static void init() {
            ReloadListenerRegistry.register(PackType.SERVER_DATA, new OxygenProductionManager());
            ReloadListenerRegistry.register(PackType.SERVER_DATA, new PlanetManager());
            EntityModelLayerRegistry.register(ChuteModel.LAYER_LOCATION, ChuteModel::createBodyLayer);
            EntityModelLayerRegistry.register(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer);
            EntityModelLayerRegistry.register(TankModel.LAYER_LOCATION, TankModel::createBodyLayer);

            PlayerEvent.CHANGE_DIMENSION.register(CommonHandler::playerChangedDimension);

            Network.initS2C();
        }
    }
}