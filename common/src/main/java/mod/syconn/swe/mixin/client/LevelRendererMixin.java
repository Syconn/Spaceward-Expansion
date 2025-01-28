package mod.syconn.swe.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import mod.syconn.swe.client.renders.debug.PipeDebugRenderer;
import mod.syconn.swe.client.renders.effects.SpaceEffect;
import mod.syconn.swe.core.ModTags;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow @Nullable
    private ClientLevel level;

    @Shadow @Nullable
    private VertexBuffer starBuffer;

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    public void renderSky(PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
        if (level.dimension() == ModTags.Planets.MOON) {
            SpaceEffect.renderSky(level, partialTick, projectionMatrix, starBuffer, skyFogSetup);
            ci.cancel();
        }
    }

    @Inject(method = "renderLevel", at = @At("TAIL"))
    public void renderSectionLayer(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        PipeDebugRenderer.renderBlockOutline(poseStack, projectionMatrix);
    }
}
