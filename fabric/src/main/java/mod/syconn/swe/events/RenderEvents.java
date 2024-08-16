package mod.syconn.swe.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;

public class RenderEvents {

    public static Event<RenderStageCallback> RENDER_STAGE_EVENT = EventFactory.createArrayBacked(RenderStageCallback.class, listeners -> (type, levelRenderer, modelViewMatrix, projectionMatrix, renderTick, partialTick, camera, frustum) -> {
        for (RenderStageCallback callback : listeners) callback.renderStage(type, levelRenderer, modelViewMatrix, projectionMatrix, renderTick, partialTick, camera, frustum);
    });

    public interface RenderStageCallback {
        void renderStage(RenderType type, LevelRenderer levelRenderer, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, int renderTick, DeltaTracker partialTick, Camera camera, Frustum frustum);
    }
}
