package mod.syconn.api.client.debug;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mod.syconn.api.client.packets.ClientBoundUpdatePipeCache;
import mod.syconn.api.world.data.savedData.PipeNetworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PipeNetworkRenderer {

    public static Map<UUID, Set<BlockPos>> PIPE_RENDERS = new HashMap<>();
    public static final Map<UUID, Integer> COLORS = new HashMap<>();
    public static boolean requestedRefresh = false;
    private static VertexBuffer vertexBuffer;
    private static int color = 0;

    public static @Nullable CustomPacketPayload playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (FMLEnvironment.dist.isDedicatedServer() && event.getEntity().level() instanceof ServerLevel sl) return new ClientBoundUpdatePipeCache(PipeNetworks.get(sl).getDataMap());
        return null;
    }

    public static @Nullable CustomPacketPayload playerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        if (FMLEnvironment.dist.isDedicatedServer() && event.getEntity().level() instanceof ServerLevel sl) return new ClientBoundUpdatePipeCache(new HashMap<>());
        return null;
    }

    public static @Nullable CustomPacketPayload playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (FMLEnvironment.dist.isDedicatedServer() && event.getEntity().level() instanceof ServerLevel sl) return new ClientBoundUpdatePipeCache(PipeNetworks.get(sl).getDataMap());
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderBlockOutline(RenderLevelStageEvent event) {
        if (RenderLevelStageEvent.Stage.AFTER_LEVEL == event.getStage()) {
            if (vertexBuffer == null || requestedRefresh) {
                requestedRefresh = false;
                vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);

                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

                var opacity = 1F;
                PIPE_RENDERS.forEach((uuid, positionList) -> {
                    int color;
                    if (COLORS.containsKey(uuid)) color = COLORS.get(uuid);
                    else {
                        color = DyeColor.byId(PipeNetworkRenderer.color).getFireworkColor();
                        COLORS.put(uuid, color);
                        PipeNetworkRenderer.color++;
                        if (PipeNetworkRenderer.color > 6) PipeNetworkRenderer.color = 0;
                    }
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

                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();

                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                RenderSystem.applyModelViewMatrix();
                RenderSystem.depthFunc(GL11.GL_ALWAYS);

                poseStack.mulPose(event.getModelViewMatrix());
                poseStack.translate(-playerPos.x(), -playerPos.y(), -playerPos.z());

                vertexBuffer.bind();
                vertexBuffer.drawWithShader(poseStack.last().pose(), event.getProjectionMatrix(), RenderSystem.getShader());
                VertexBuffer.unbind();
                RenderSystem.depthFunc(GL11.GL_LEQUAL);

                poseStack.popPose();
                RenderSystem.applyModelViewMatrix();
            }
        }
    }
}
