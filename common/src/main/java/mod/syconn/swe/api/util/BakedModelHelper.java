package mod.syconn.swe.api.util;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;

public class BakedModelHelper {

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
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer();
        builder.setSprite(sprite);
        putVertex(builder, normal, (float) v1.x, (float) v1.y, (float) v1.z, 0, 0, sprite);
        putVertex(builder, normal, (float) v2.x, (float) v2.y, (float) v2.z, 0, 1, sprite);
        putVertex(builder, normal, (float) v3.x, (float) v3.y, (float) v3.z, 1, 1, sprite);
        putVertex(builder, normal, (float) v4.x, (float) v4.y, (float)v4.z, 1, 0, sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        return builder.bakeQuad();
    }

    private static void putVertex(QuadBakingVertexConsumer builder, Position normal, float x, float y, float z, float u, float v, TextureAtlasSprite sprite) {
        float iu = sprite.getU(u);
        float iv = sprite.getV(v);
        builder.addVertex(x, y, z);
        builder.setUv(iu, iv);
        builder.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        builder.setUv2(0, 0);
        builder.setNormal((float) normal.x(), (float) normal.y(), (float) normal.z());
    }

    public static Vec3 v(double x, double y, double z) {
        return new Vec3(x, y, z);
    }
}
