package mod.syconn.swe.forge;

import dev.architectury.platform.forge.EventBuses;
import mod.syconn.swe.Constants;
import mod.syconn.swe.SpaceMod;
import mod.syconn.swe.client.renders.entity.layer.SpaceSuitLayer;
import mod.syconn.swe.forge.client.PipeModelLoader;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD)
public final class SpaceModForge {

    public SpaceModForge() {
        EventBuses.registerModEventBus(Constants.MOD, FMLJavaModLoadingContext.get().getModEventBus());
        SpaceMod.init();
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Client {

        @SubscribeEvent
        public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register("pipe", new PipeModelLoader());
        }

        @SubscribeEvent
        public static void addRenderLayers(EntityRenderersEvent.AddLayers event) {
            addPlayerLayers(event.getSkin("default"), event.getEntityModels());
            addPlayerLayers(event.getSkin("slim"), event.getEntityModels());
        }

        private static void addPlayerLayers(EntityRenderer<? extends Player> renderer, EntityModelSet s) {
            if(renderer instanceof PlayerRenderer playerRenderer) playerRenderer.addLayer(new SpaceSuitLayer<>(playerRenderer, s));
        }
    }
}
