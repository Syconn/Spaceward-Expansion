package mod.syconn.swe.util.core;

import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.multiplayer.ClientLevel;
import org.joml.Matrix4f;

public interface CustomSkyRenderer {

    boolean renderSky(ClientLevel level, float partialTick, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, VertexBuffer starBuffer, Runnable setupFog);
}
