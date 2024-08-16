package mod.syconn.swe.mixin;

import mod.syconn.swe.events.RenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRenderMixin {

    @Shadow
    private int ticks;

    @Shadow @Final
    private Minecraft minecraft;

    @Shadow @Nullable
    private Frustum capturedFrustum;

    @Shadow
    private Frustum cullingFrustum;

    @Inject(method = "renderSectionLayer", at = @At("TAIL"))
    public void renderSectionLayer(RenderType renderType, double x, double y, double z, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        RenderEvents.RENDER_STAGE_EVENT.invoker().renderStage(renderType, (LevelRenderer) (Object) this, modelViewMatrix, projectionMatrix, this.ticks, this.minecraft.getTimer(), this.minecraft.gameRenderer.getMainCamera(), this.capturedFrustum != null ? this.capturedFrustum : this.cullingFrustum);
    }
}
