package mod.syconn.swe.fabric.client;

import mod.syconn.swe.Constants;
import mod.syconn.swe.client.renders.entity.layer.SpaceSuitLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public final class SpaceModFabricClient implements ClientModInitializer {

    public void onInitializeClient() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if(entityRenderer instanceof PlayerRenderer renderer) registrationHelper.register(new SpaceSuitLayer<>(renderer, context.getModelSet()));
        });

        ModelLoadingPlugin.register(new ModelLoader());
    }

    @Environment(EnvType.CLIENT)
    private static class ModelLoader implements ModelLoadingPlugin {

        public void onInitializeModelLoader(Context pluginContext) {
            pluginContext.modifyModelOnLoad().register(((model, context) -> {
                if (context.id() != null && context.id().equals(Constants.withId("item/fluid_pipe"))) return new PipeModelLoader();
                return model;
            }));
            pluginContext.modifyModelBeforeBake().register((original, context) -> {
                if (context.id() != null && context.id().equals(Constants.withId("block/fluid_pipe"))) return new PipeModelLoader();
                return original;
            });
        }
    }
}
