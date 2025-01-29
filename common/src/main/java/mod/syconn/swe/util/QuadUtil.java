package mod.syconn.swe.util;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class QuadUtil {

    public static BakedQuad quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int rotation) {
        return switch (rotation) {
            case 1 -> quad(v2, v3, v4, v1, sprite);
            case 2 -> quad(v3, v4, v1, v2, sprite);
            case 3 -> quad(v4, v1, v2, v3, sprite);
            default -> quad(v1, v2, v3, v4, sprite);
        };
    }

    public static BakedQuad quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        BakedQuad[] quad = new BakedQuad[1];
        QuadVertexConsumer builder = new QuadVertexConsumer(q -> quad[0] = q);
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        putVertex(builder, normal, (float) v1.x, (float) v1.y, (float) v1.z, 0, 0, sprite);
        putVertex(builder, normal, (float) v2.x, (float) v2.y, (float) v2.z, 0, 1, sprite);
        putVertex(builder, normal, (float) v3.x, (float) v3.y, (float) v3.z, 1, 1, sprite);
        putVertex(builder, normal, (float) v4.x, (float) v4.y, (float)v4.z, 1, 0, sprite);
        return quad[0];
    }

    private static void putVertex(QuadVertexConsumer builder, Position normal, float x, float y, float z, float u, float v, TextureAtlasSprite sprite) {
        float iu = sprite.getU(u);
        float iv = sprite.getV(v);
        builder.vertex(x, y, z);
        builder.uv(iu, iv);
        builder.color(1.0f, 1.0f, 1.0f, 1.0f);
        builder.uv2(0, 0);
        builder.normal((float) normal.x(), (float) normal.y(), (float) normal.z());
    }

    public static Vec3 v(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

    private static class QuadVertexConsumer implements VertexConsumer {
        private static final int QUAD_DATA_SIZE = IQuadTransformer.STRIDE * 4;

        private final Consumer<BakedQuad> quadConsumer;
        protected int vertexIndex = 0;
        private int[] quadData = new int[QUAD_DATA_SIZE];
        private Direction direction = Direction.DOWN;
        private TextureAtlasSprite sprite = UnitTextureAtlasSprite.INSTANCE;

        public QuadVertexConsumer(Consumer<BakedQuad> quadConsumer)
        {
            this.quadConsumer = quadConsumer;
        }

        public VertexConsumer vertex(double x, double y, double z) {
            int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
            quadData[offset] = Float.floatToRawIntBits((float) x);
            quadData[offset + 1] = Float.floatToRawIntBits((float) y);
            quadData[offset + 2] = Float.floatToRawIntBits((float) z);
            return this;
        }

        public VertexConsumer normal(float x, float y, float z) {
            int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.NORMAL;
            quadData[offset] = ((int) (x * 127.0f) & 0xFF) | (((int) (y * 127.0f) & 0xFF) << 8) | (((int) (z * 127.0f) & 0xFF) << 16);
            return this;
        }

        public VertexConsumer color(int r, int g, int b, int a) {
            int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.COLOR;
            quadData[offset] = ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((g & 0xFF) << 8) | (r & 0xFF);
            return this;
        }

        public VertexConsumer uv(float u, float v) {
            int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
            quadData[offset] = Float.floatToRawIntBits(u);
            quadData[offset + 1] = Float.floatToRawIntBits(v);
            return this;
        }

        public VertexConsumer overlayCoords(int u, int v) {
            if (IQuadTransformer.UV1 >= 0) {
                int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV1;
                quadData[offset] = (u & 0xFFFF) | ((v & 0xFFFF) << 16);
            }
            return this;
        }

        public VertexConsumer uv2(int u, int v) {
            int offset = vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV2;
            quadData[offset] = (u & 0xFFFF) | ((v & 0xFFFF) << 16);
            return this;
        }

        public void endVertex() {
            if (++vertexIndex != 4) return;
            quadConsumer.accept(new BakedQuad(quadData, -1, direction, sprite, false));
            vertexIndex = 0;
            quadData = new int[QUAD_DATA_SIZE];
        }

        public void defaultColor(int r, int g, int b, int a) { }

        public void unsetDefaultColor() { }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public void setSprite(TextureAtlasSprite sprite) {
            this.sprite = sprite;
        }
    }

    private interface IQuadTransformer
    {
        int STRIDE = DefaultVertexFormat.BLOCK.getIntegerSize();
        int POSITION = findOffset(DefaultVertexFormat.ELEMENT_POSITION);
        int COLOR = findOffset(DefaultVertexFormat.ELEMENT_COLOR);
        int UV0 = findOffset(DefaultVertexFormat.ELEMENT_UV0);
        int UV1 = findOffset(DefaultVertexFormat.ELEMENT_UV1);
        int UV2 = findOffset(DefaultVertexFormat.ELEMENT_UV2);
        int NORMAL = findOffset(DefaultVertexFormat.ELEMENT_NORMAL);

        private static int findOffset(VertexFormatElement element) {
            var index = DefaultVertexFormat.BLOCK.getElements().indexOf(element);
            return index < 0 ? -1 : DefaultVertexFormat.BLOCK.offsets.getInt(index) / 4;
        }
    }

    private static class UnitTextureAtlasSprite extends TextureAtlasSprite {
        public static final ResourceLocation LOCATION = new ResourceLocation("forge", "unit");
        public static final UnitTextureAtlasSprite INSTANCE = new UnitTextureAtlasSprite();

        private UnitTextureAtlasSprite()
        {
            super(LOCATION, new SpriteContents(LOCATION, new FrameSize(1, 1), new NativeImage(1, 1, false), AnimationMetadataSection.EMPTY), 1, 1, 0, 0);
        }

        @Override
        public float getU(double u)
        {
            return (float) u / 16;
        }

        @Override
        public float getV(double v)
        {
            return (float) v / 16;
        }
    }
}
