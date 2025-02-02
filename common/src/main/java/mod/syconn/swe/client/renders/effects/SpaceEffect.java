package mod.syconn.swe.client.renders.effects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import mod.syconn.swe.Constants;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class SpaceEffect {

    private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation EARTH_LOCATION = Constants.withId("textures/environment/earth.png");
    private static final ResourceLocation SKY_LOCATION = Constants.withId("textures/environment/sky.png");

    public static void renderSky(ClientLevel level, float partialTick, Matrix4f projectionMatrix, VertexBuffer starBuffer, Runnable setupFog) {
        PoseStack posestack = new PoseStack();
//        posestack.mulPose(modelViewMatrix); TODO DO I NEED
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, SKY_LOCATION);
        Tesselator tesselator = Tesselator.getInstance();

        for (int i = 0; i < 6; i++) {
            posestack.pushPose();
            if (i == 1) posestack.mulPose(Axis.XP.rotationDegrees(90.0F));
            if (i == 2) posestack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            if (i == 3) posestack.mulPose(Axis.XP.rotationDegrees(180.0F));
            if (i == 4) posestack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            if (i == 5) posestack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            Matrix4f matrix4f = posestack.last().pose();
            BufferBuilder bufferBuilder = tesselator.getBuilder();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.0F, 0.0F).color(-14145496);
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.0F, 16.0F).color(-14145496);
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(16.0F, 16.0F).color(-14145496);
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(16.0F, 0.0F).color(-14145496);
            BufferUploader.drawWithShader(bufferBuilder.end());
            posestack.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        posestack.pushPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        posestack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        posestack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 120.0F));
        Matrix4f matrix4f1 = posestack.last().pose();
        float f12 = 30.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SUN_LOCATION);
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F);
        bufferBuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F);
        bufferBuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F);
        bufferBuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F);
        BufferUploader.drawWithShader(bufferBuilder.end());
        RenderSystem.setShaderTexture(0, EARTH_LOCATION);
        bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(matrix4f1, -f12, -100.0F, f12).uv(0.0F, 0.0F);
        bufferBuilder.vertex(matrix4f1, f12, -100.0F, f12).uv(1.0F, 0.0F);
        bufferBuilder.vertex(matrix4f1, f12, -100.0F, -f12).uv(1.0F, 1.0F);
        bufferBuilder.vertex(matrix4f1, -f12, -100.0F, -f12).uv(0.0F, 1.0F);
        BufferUploader.drawWithShader(bufferBuilder.end());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        float starBrightness = 1.0F;
        RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
        starBuffer.bind();
        starBuffer.drawWithShader(posestack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
        VertexBuffer.unbind();
        setupFog.run();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        posestack.popPose();
        RenderSystem.depthMask(true);
    }
}
