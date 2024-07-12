package mod.syconn.swe.util.worldgen.dimension;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import mod.syconn.swe.Main;

public class MoonSpecialEffects extends DimensionSpecialEffects {

    private static final ResourceLocation SUN_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/sun.png");
    private static final ResourceLocation EARTH_LOCATION = Main.loc("textures/environment/earth.png");
    private static final ResourceLocation SKY_LOCATION = Main.loc("textures/environment/sky.png");

    private final Minecraft minecraft = Minecraft.getInstance();

    public MoonSpecialEffects() {
        super(Float.NaN, false, SkyType.END, true, false);
    }

    public Vec3 getBrightnessDependentFogColor(Vec3 pFogColor, float pBrightness) {
        return pFogColor.multiply(pBrightness * 0.94F + 0.06F, pBrightness * 0.94F + 0.06F, pBrightness * 0.91F + 0.09F);
    }

    public boolean isFoggyAt(int pX, int pY) {
        return false;
    }

    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        PoseStack posestack = new PoseStack();
        posestack.mulPose(modelViewMatrix);
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, SKY_LOCATION);
        Tesselator tesselator = Tesselator.getInstance();

        for (int i = 0; i < 6; i++) {
            posestack.pushPose();
            if (i == 1) {
                posestack.mulPose(Axis.XP.rotationDegrees(90.0F));
            }

            if (i == 2) {
                posestack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            }

            if (i == 3) {
                posestack.mulPose(Axis.XP.rotationDegrees(180.0F));
            }

            if (i == 4) {
                posestack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }

            if (i == 5) {
                posestack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            }

            Matrix4f matrix4f = posestack.last().pose();
            BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F).setColor(-14145496);
            bufferbuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F).setColor(-14145496);
            bufferbuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F).setColor(-14145496);
            bufferbuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F).setColor(-14145496);
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
            posestack.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        //        PoseStack poseStack = new PoseStack();
//        RenderSystem.enableBlend();
//        RenderSystem.depthMask(false);
//        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
//        RenderSystem.setShaderTexture(0, SKY_LOCATION);
//        Tesselator tesselator = Tesselator.getInstance();
//
//        for(int i = 0; i < 6; ++i) {
//            poseStack.pushPose();
//            if (i == 1) poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
//            if (i == 2) poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
//            if (i == 3) poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
//            if (i == 4) poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
//            if (i == 5) poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
//            Matrix4f matrix4f = poseStack.last().pose();
//            BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
//            int brightness = 50;
//            bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F).setColor(brightness, brightness, brightness, 255);
//            bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F).setColor(brightness, brightness, brightness, 255);
//            bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F).setColor(brightness, brightness, brightness, 255);
//            bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F).setColor(brightness, brightness, brightness, 255);
//            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
//            poseStack.popPose();
//        }

//        poseStack.pushPose();
//        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
//        poseStack.mulPose(Axis.XP.rotationDegrees(60.0F));
//        Matrix4f matrix4f = poseStack.last().pose();
//        float f12 = 30.0F; //SIZE
//        int brightness = 50;
//        RenderSystem.setShaderColor(0.85F, 0.85F, 0.85F, 0.85F);
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, EARTH_LOCATION);
//        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
//        bufferBuilder.addVertex(matrix4f, -f12, 100.0F, -f12).setUv(0.0F, 0.0F).setColor(brightness, brightness, brightness, 255);
//        bufferBuilder.addVertex(matrix4f, f12, 100.0F, -f12).setUv(1.0F, 0.0F).setColor(brightness, brightness, brightness, 255);
//        bufferBuilder.addVertex(matrix4f, f12, 100.0F, f12).setUv(1.0F, 1.0F).setColor(brightness, brightness, brightness, 255);
//        bufferBuilder.addVertex(matrix4f, -f12, 100.0F, f12).setUv(0.0F, 1.0F).setColor(brightness, brightness, brightness, 255);
//        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.disableBlend();
//        RenderSystem.defaultBlendFunc();
//        poseStack.popPose();
//        RenderSystem.depthMask(true);
//        RenderSystem.disableBlend();
        // TODO NEW CODE BELOW
//        RenderSystem.blendFuncSeparate(
//                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
//        );
//        poseStack.pushPose();
//        float f11 = 1.0F; // - this.level.getRainLevel(pPartialTick)
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
//        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
//        poseStack.mulPose(Axis.XP.rotationDegrees(360.0F));
//        Matrix4f matrix4f1 = poseStack.last().pose();
//        float f12 = 30.0F;
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, SUN_LOCATION);
//        BufferBuilder bufferbuilder1 = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        bufferbuilder1.addVertex(matrix4f1, -f12, 100.0F, -f12).setUv(0.0F, 0.0F);
//        bufferbuilder1.addVertex(matrix4f1, f12, 100.0F, -f12).setUv(1.0F, 0.0F);
//        bufferbuilder1.addVertex(matrix4f1, f12, 100.0F, f12).setUv(1.0F, 1.0F);
//        bufferbuilder1.addVertex(matrix4f1, -f12, 100.0F, f12).setUv(0.0F, 1.0F);
//        BufferUploader.drawWithShader(bufferbuilder1.buildOrThrow());
//        f12 = 20.0F;
////        RenderSystem.setShaderTexture(0, MOON_LOCATION);
////        int k = this.level.getMoonPhase();
////        int l = k % 4;
////        int i1 = k / 4 % 2;
////        float f13 = (float)(l + 0) / 4.0F;
////        float f14 = (float)(i1 + 0) / 2.0F;
////        float f15 = (float)(l + 1) / 4.0F;
////        float f16 = (float)(i1 + 1) / 2.0F;
////        bufferbuilder1 = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
////        bufferbuilder1.addVertex(matrix4f1, -f12, -100.0F, f12).setUv(f15, f16);
////        bufferbuilder1.addVertex(matrix4f1, f12, -100.0F, f12).setUv(f13, f16);
////        bufferbuilder1.addVertex(matrix4f1, f12, -100.0F, -f12).setUv(f13, f14);
////        bufferbuilder1.addVertex(matrix4f1, -f12, -100.0F, -f12).setUv(f15, f14);
////        BufferUploader.drawWithShader(bufferbuilder1.buildOrThrow());
//        float f10 = level.getStarBrightness(partialTick) * f11;
//        if (f10 > 0.0F) {
//            RenderSystem.setShaderColor(f10, f10, f10, f10);
//            FogRenderer.setupNoFog();
//            this.starBuffer.bind();
//            this.starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
//            VertexBuffer.unbind();
//            setupFog.run();
//        }
//
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.disableBlend();
//        RenderSystem.defaultBlendFunc();
//        poseStack.popPose();
//        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
//        double d0 = this.minecraft.player.getEyePosition(partialTick).y - level.getLevelData().getHorizonHeight(level);
//        if (d0 < 0.0) {
//            poseStack.pushPose();
//            poseStack.translate(0.0F, 12.0F, 0.0F);
//            this.darkBuffer.bind();
//            this.darkBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
//            VertexBuffer.unbind();
//            poseStack.popPose();
//        }
//
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.depthMask(true);
        return true;
    }
}
