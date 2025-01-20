package mod.syconn.swe;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import mod.syconn.swe.core.*;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.server.reloaders.OxygenProductionManager;
import mod.syconn.swe.server.reloaders.PlanetManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.packs.PackType;

public class SpaceMod {

    public static void init() {
        ModFluids.FLUIDS.register();
        ModItems.ITEMS.register();
        ModBlocks.BLOCKS.register();
        ModBlockEntities.BLOCK_ENTITIES.register();
        ModItems.TABS.register();
        ModMenus.MENUS.register();
        ModRecipes.RECIPE_SERIALIZERS.register();

        CreativeTabRegistry.modify(ModItems.TAB, ModItems::addCreative);


        Network.init();
        EnvExecutor.runInEnv(Env.CLIENT, () -> Client::init);
        EnvExecutor.runInEnv(Env.SERVER, () -> Server::init);
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        @Environment(EnvType.CLIENT)
        public static void init() {
            ClientLifecycleEvent.CLIENT_SETUP.register(ModMenus::registerScreens);
            ClientTickEvent.CLIENT_PRE.register(ClientHandler::onClientPlayerTick);
            ClientGuiEvent.RENDER_HUD.register(IronmanOverlay::renderOverlay);

            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Constants.withId("animation"), 42, Client::registerPlayerAnimation);
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
        }
    }
}