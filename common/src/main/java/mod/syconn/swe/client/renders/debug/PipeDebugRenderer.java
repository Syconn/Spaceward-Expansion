package mod.syconn.swe.client.renders.debug;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mod.syconn.swe.network.messages.ClientBoundUpdatePipeCache;
import mod.syconn.swe.server.savedData.PipeNetworks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PipeDebugRenderer {

    public static Map<UUID, Set<BlockPos>> PIPE_RENDERS = new HashMap<>();
    public static boolean requestedRefresh = false;
    private static VertexBuffer vertexBuffer;
    private static final int color = 0;

    @Environment(EnvType.SERVER)

    public static ClientBoundUpdatePipeCache playerJoined(ServerPlayer player) {
        return new ClientBoundUpdatePipeCache(PipeNetworks.get(player.serverLevel()).getDataMap());
    }

    @Environment(EnvType.SERVER)
    public static ClientBoundUpdatePipeCache playerLeft() {
        return new ClientBoundUpdatePipeCache(new HashMap<>());
    }

    @Environment(EnvType.SERVER)
    public static ClientBoundUpdatePipeCache playerChangedDimension(ServerPlayer player) {
        return new ClientBoundUpdatePipeCache(PipeNetworks.get(player.serverLevel()).getDataMap());
    }

    public static void renderBlockOutline(Events.LevelRenderStage event) {
        if (vertexBuffer == null || requestedRefresh) {
            requestedRefresh = false;
            vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);

            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

            var opacity = 1F;
            PIPE_RENDERS.forEach((uuid, positionList) -> {
                int color = uuidToRGBA(uuid);
                positionList.forEach(pos -> {
                    final float size = 1.0f;
                    final int x = pos.getX(), y = pos.getY(), z = pos.getZ();

                    final float red = (color >> 16 & 0xff) / 255f;
                    final float green = (color >> 8 & 0xff) / 255f;
                    final float blue = (color & 0xff) / 255f;

                    buffer.addVertex(x, y + size, z).setColor(red, green, blue, opacity);
                    buffer.addVertex(x + size, y + size, z).setColor(red, green, blue, opacity);
                    buffer.addVertex(x + size, y + size, z).setColor(red, green, blue, opacity);
                    buffer.addVertex(x + size, y + size, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x + size, y + size, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x, y + size, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x, y + size, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x, y + size, z).setColor(red, green, blue, opacity);

                    // BOTTOM
                    buffer.addVertex(x + size, y, z).setColor(red, green, blue, opacity);
                    buffer.addVertex(x + size, y, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x + size, y, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x, y, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x, y, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x, y, z).setColor(red, green, blue, opacity);
                    buffer.addVertex(x, y, z).setColor(red, green, blue, opacity);
                    buffer.addVertex(x + size, y, z).setColor(red, green, blue, opacity);

                    // Edge 1
                    buffer.addVertex(x + size, y, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x + size, y + size, z + size).setColor(red, green, blue, opacity);

                    // Edge 2
                    buffer.addVertex(x + size, y, z).setColor(red, green, blue, opacity);
                    buffer.addVertex(x + size, y + size, z).setColor(red, green, blue, opacity);

                    // Edge 3
                    buffer.addVertex(x, y, z + size).setColor(red, green, blue, opacity);
                    buffer.addVertex(x, y + size, z + size).setColor(red, green, blue, opacity);

                    // Edge 4
                    buffer.addVertex(x, y, z).setColor(red, green, blue, opacity);
                    buffer.addVertex(x, y + size, z).setColor(red, green, blue, opacity);
                });
            });

            MeshData build = buffer.build();
            if (build == null) {
                vertexBuffer = null;
                return;
            } else {
                vertexBuffer.bind();
                vertexBuffer.upload(build);
                VertexBuffer.unbind();
            }
        }

        if (vertexBuffer != null) {
            Vec3 playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            PoseStack poseStack = event.poseStack();
            poseStack.pushPose();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.applyModelViewMatrix();
            RenderSystem.depthFunc(GL11.GL_ALWAYS);

            poseStack.mulPose(event.modelViewMatrix());
            poseStack.translate(-playerPos.x(), -playerPos.y(), -playerPos.z());

            vertexBuffer.bind();
            vertexBuffer.drawWithShader(poseStack.last().pose(), event.projectionMatrix(), RenderSystem.getShader());
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
        return FastColor.ARGB32.color(r, g, b);
    }
}
