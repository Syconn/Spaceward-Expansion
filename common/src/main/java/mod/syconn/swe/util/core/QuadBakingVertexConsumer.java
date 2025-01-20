package mod.syconn.swe.util.core;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;

import java.util.Arrays;

/**
 * Forge Version - Made Compatible
 **/
public class QuadBakingVertexConsumer implements VertexConsumer {
    private static final int QUAD_DATA_SIZE = IQuadTransformer.STRIDE * 4;

    private final int[] quadData = new int[QUAD_DATA_SIZE];
    private int vertexIndex = 0;
    private boolean building = false;

    private int tintIndex = -1;
    private Direction direction = Direction.DOWN;
    private TextureAtlasSprite sprite = UnitTextureAtlasSprite.INSTANCE;
    private boolean shade;

    public VertexConsumer addVertex(float x, float y, float z) {
        if (building) {
            if (++vertexIndex > 4) {
                throw new IllegalStateException("Expected quad export after fourth vertex");
            }
        }
        building = true;

        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
        quadData[offset] = Float.floatToRawIntBits(x);
        quadData[offset + 1] = Float.floatToRawIntBits(y);
        quadData[offset + 2] = Float.floatToRawIntBits(z);
        return this;
    }

    public VertexConsumer setNormal(float x, float y, float z) {
        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.NORMAL;
        quadData[offset] = ((int) (x * 127.0f) & 0xFF) |
                (((int) (y * 127.0f) & 0xFF) << 8) |
                (((int) (z * 127.0f) & 0xFF) << 16);
        return this;
    }

    public VertexConsumer setColor(int r, int g, int b, int a) {
        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.COLOR;
        quadData[offset] = ((a & 0xFF) << 24) |
                ((b & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (r & 0xFF);
        return this;
    }

    public VertexConsumer setUv(float u, float v) {
        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
        quadData[offset] = Float.floatToRawIntBits(u);
        quadData[offset + 1] = Float.floatToRawIntBits(v);
        return this;
    }

    public VertexConsumer setUv1(int u, int v) {
        if (IQuadTransformer.UV1 >= 0) { // Vanilla doesn't support this, but it may be added by a 3rd party
            int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV1;
            quadData[offset] = (u & 0xFFFF) | ((v & 0xFFFF) << 16);
        }
        return this;
    }

    public VertexConsumer setUv2(int u, int v) {
        int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV2;
        quadData[offset] = (u & 0xFFFF) | ((v & 0xFFFF) << 16);
        return this;
    }

    public void setTintIndex(int tintIndex) {
        this.tintIndex = tintIndex;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSprite(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    public BakedQuad bakeQuad() {
        if (!building || ++vertexIndex != 4) {
            throw new IllegalStateException("Not enough vertices available. Vertices in buffer: " + vertexIndex);
        }

        BakedQuad quad = new BakedQuad(quadData.clone(), tintIndex, direction, sprite, shade);
        vertexIndex = 0;
        building = false;
        Arrays.fill(quadData, 0);
        return quad;
    }

    private static class UnitTextureAtlasSprite extends TextureAtlasSprite {
        public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath("neoforge", "unit");
        public static final UnitTextureAtlasSprite INSTANCE = new UnitTextureAtlasSprite();

        private UnitTextureAtlasSprite() {
            super(LOCATION, new SpriteContents(LOCATION, new FrameSize(1, 1), new NativeImage(1, 1, false), ResourceMetadata.EMPTY), 1, 1, 0, 0);
        }

        public float getU(float u) {
            return u;
        }

        public float getV(float v) {
            return v;
        }
    }

    private interface IQuadTransformer {
        int STRIDE = DefaultVertexFormat.BLOCK.getVertexSize() / 4;
        int POSITION = findOffset(VertexFormatElement.POSITION);
        int COLOR = findOffset(VertexFormatElement.COLOR);
        int UV0 = findOffset(VertexFormatElement.UV0);
        int UV1 = findOffset(VertexFormatElement.UV1);
        int UV2 = findOffset(VertexFormatElement.UV2);
        int NORMAL = findOffset(VertexFormatElement.NORMAL);

        private static int findOffset(VertexFormatElement element) {
            if (DefaultVertexFormat.BLOCK.contains(element)) return DefaultVertexFormat.BLOCK.getOffset(element) / 4;
            return -1;
        }
    }
}
