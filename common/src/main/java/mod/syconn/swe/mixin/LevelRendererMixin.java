package mod.syconn.swe.mixin;

import com.mojang.blaze3d.vertex.VertexBuffer;
import mod.syconn.swe.extra.core.CustomSkyRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
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
    public VertexBuffer starBuffer;

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    public void renderSky(Matrix4f pFrustumMatrix, Matrix4f pProjectionMatrix, float pPartialTick, Camera pCamera, boolean pIsFoggy, Runnable pSkyFogSetup, CallbackInfo ci) {
        if (level.effects() instanceof CustomSkyRenderer sky && sky.renderSky(level, pPartialTick, pFrustumMatrix, pProjectionMatrix, starBuffer, pSkyFogSetup)) ci.cancel();
    }
}
