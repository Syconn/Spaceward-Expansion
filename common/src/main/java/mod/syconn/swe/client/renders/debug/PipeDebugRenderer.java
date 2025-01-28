package mod.syconn.swe.client.renders.debug;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mod.syconn.swe.network.messages.MessageUpdateClientPipeCache;
import mod.syconn.swe.server.savedData.PipeNetworks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PipeDebugRenderer {

    public static Map<UUID, Set<BlockPos>> PIPE_RENDERS = new HashMap<>();
    public static boolean requestedRefresh = false;
    private static VertexBuffer vertexBuffer;

    @Environment(EnvType.SERVER)

    public static MessageUpdateClientPipeCache playerJoined(ServerPlayer player) {
        return new MessageUpdateClientPipeCache(PipeNetworks.get(player.serverLevel()).getDataMap());
    }

    @Environment(EnvType.SERVER)
    public static MessageUpdateClientPipeCache playerLeft() {
        return new MessageUpdateClientPipeCache(new HashMap<>());
    }

    @Environment(EnvType.SERVER)
    public static MessageUpdateClientPipeCache playerChangedDimension(ServerPlayer player) {
        return new MessageUpdateClientPipeCache(PipeNetworks.get(player.serverLevel()).getDataMap());
    }

    public static void renderBlockOutline(PoseStack poseStack, Matrix4f projectionMatrix) {
        if (vertexBuffer == null || requestedRefresh) {
            requestedRefresh = false;
            vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

            var opacity = 1F;
            PIPE_RENDERS.forEach((uuid, positionList) -> {
                int color = uuidToRGBA(uuid);
                positionList.forEach(pos -> {
                    final float size = 1.0f;
                    final int x = pos.getX(), y = pos.getY(), z = pos.getZ();

                    final float red = (color >> 16 & 0xff) / 255f;
                    final float green = (color >> 8 & 0xff) / 255f;
                    final float blue = (color & 0xff) / 255f;

                    buffer.vertex(x, y + size, z).color(red, green, blue, opacity);
                    buffer.vertex(x + size, y + size, z).color(red, green, blue, opacity);
                    buffer.vertex(x + size, y + size, z).color(red, green, blue, opacity);
                    buffer.vertex(x + size, y + size, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x + size, y + size, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x, y + size, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x, y + size, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x, y + size, z).color(red, green, blue, opacity);

                    // BOTTOM
                    buffer.vertex(x + size, y, z).color(red, green, blue, opacity);
                    buffer.vertex(x + size, y, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x + size, y, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x, y, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x, y, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x, y, z).color(red, green, blue, opacity);
                    buffer.vertex(x, y, z).color(red, green, blue, opacity);
                    buffer.vertex(x + size, y, z).color(red, green, blue, opacity);

                    // Edge 1
                    buffer.vertex(x + size, y, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x + size, y + size, z + size).color(red, green, blue, opacity);

                    // Edge 2
                    buffer.vertex(x + size, y, z).color(red, green, blue, opacity);
                    buffer.vertex(x + size, y + size, z).color(red, green, blue, opacity);

                    // Edge 3
                    buffer.vertex(x, y, z + size).color(red, green, blue, opacity);
                    buffer.vertex(x, y + size, z + size).color(red, green, blue, opacity);

                    // Edge 4
                    buffer.vertex(x, y, z).color(red, green, blue, opacity);
                    buffer.vertex(x, y + size, z).color(red, green, blue, opacity);
                });
            });

            BufferBuilder.RenderedBuffer build = buffer.end();
//            if (build == null) {
//                vertexBuffer = null;
//                return;
//            } else { TODO DO I NEED AGAIN
                vertexBuffer.bind();
                vertexBuffer.upload(build);
                VertexBuffer.unbind();
//            }
        }

        if (vertexBuffer != null) {
            Vec3 playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();


            poseStack.pushPose();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.applyModelViewMatrix();
            RenderSystem.depthFunc(GL11.GL_ALWAYS);
//            poseStack.mulPose(event.modelViewMatrix()); TODO AGAIN DO I NEED
            poseStack.translate(-playerPos.x(), -playerPos.y(), -playerPos.z());
            vertexBuffer.bind();
            vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, RenderSystem.getShader());
            VertexBuffer.unbind();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            poseStack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    public static int uuidToRGBA(UUID uuid) {
        long mostSigBits = uuid.getMostSignificantBits();
        int r = (int) ((mostSigBits >> 32) & 0xFF);
        int g = (int) ((mostSigBits >> 16) & 0xFF);
        int b = (int) (mostSigBits & 0xFF);
        return FastColor.ARGB32.color(256, r, g, b);
    }
}
