package mod.syconn.swe.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.syconn.api.util.PipeConnectionTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Map;

public class RenderUtil {

    public static int getFluidColor(FluidStack fluidStack) {
        if (fluidStack.isEmpty()) return -1;
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluidType());
        int i = props.getTintColor(fluidStack);
        TextureAtlasSprite sprite = getSprite(fluidStack);
        int b = sprite.getPixelRGBA(0, 8, 8);
        int c = FastColor.ARGB32.color(FastColor.ARGB32.blue(b), FastColor.ARGB32.green(b), FastColor.ARGB32.red(b));
        if (i == -1) return c;
        return tintRGBA(c, i);
    }

    private static int tintRGBA(int color, int tintColor) {
        int r1 = FastColor.ARGB32.red(color);
        int g1 = FastColor.ARGB32.green(color);
        int b1 = FastColor.ARGB32.blue(color);
        int r2 = FastColor.ARGB32.red(tintColor);
        int g2 = FastColor.ARGB32.green(tintColor);
        int b2 = FastColor.ARGB32.blue(tintColor);
        int r = (r1 * (255 - 230) + r2 * 230) / 255;
        int g = (g1 * (255 - 230) + g2 * 230) / 255;
        int b = (b1 * (255 - 230) + b2 * 230) / 255;
        return FastColor.ARGB32.color(FastColor.ARGB32.alpha(color), r, g, b);
    }

    private static TextureAtlasSprite getSprite(ResourceLocation texture) { // TODO if works cache colors
        Map<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getTextures();
        return atlas.getOrDefault(texture, atlas.get(MissingTextureAtlasSprite.getLocation()));
    }

    public static TextureAtlasSprite getSprite(FluidStack fluidStack) { // TODO if works cache colors
        if (fluidStack.isEmpty()) return getSprite(MissingTextureAtlasSprite.getLocation());
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluidType());
        return getSprite(props.getStillTexture(fluidStack));
    }

    public static void renderLiquid(PoseStack pPoseStack, MultiBufferSource pBufferSource, Fluid fluid, float minScale, float maxScale, float height) {
        if (!fluid.isSame(Fluids.EMPTY)) {
            IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(fluid);
            ResourceLocation fluidStill = extension.getStillTexture();
            int tint = extension.getTintColor(new FluidStack(fluid, 1));
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
            VertexConsumer builder = pBufferSource.getBuffer(RenderType.translucent());

            pPoseStack.pushPose();
            // Top Face
            if (height < 0.99f) {
                add(builder, pPoseStack, minScale, height, maxScale, sprite.getU0(), sprite.getV1(), tint);
                add(builder, pPoseStack, maxScale, height, maxScale, sprite.getU1(), sprite.getV1(), tint);
                add(builder, pPoseStack, maxScale, height, minScale, sprite.getU1(), sprite.getV0(), tint);
                add(builder, pPoseStack, minScale, height, minScale, sprite.getU0(), sprite.getV0(), tint);
            }

            // Front Faces [NORTH - SOUTH]
            add(builder, pPoseStack, maxScale, height, maxScale, sprite.getU0(), sprite.getV0(), tint);
            add(builder, pPoseStack, minScale, height, maxScale, sprite.getU1(), sprite.getV0(), tint);
            add(builder, pPoseStack, minScale, minScale, maxScale, sprite.getU1(), sprite.getV1(), tint);
            add(builder, pPoseStack, maxScale, minScale, maxScale, sprite.getU0(), sprite.getV1(), tint);

            add(builder, pPoseStack, maxScale, minScale, minScale, sprite.getU0(), sprite.getV1(), tint);
            add(builder, pPoseStack, minScale, minScale, minScale, sprite.getU1(), sprite.getV1(), tint);
            add(builder, pPoseStack, minScale, height, minScale, sprite.getU1(), sprite.getV0(), tint);
            add(builder, pPoseStack, maxScale, height, minScale, sprite.getU0(), sprite.getV0(), tint);

            pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
            pPoseStack.translate(-1f, 0, 0);

            add(builder, pPoseStack, maxScale, height, maxScale, sprite.getU0(), sprite.getV0(), tint);
            add(builder, pPoseStack, minScale, height, maxScale, sprite.getU1(), sprite.getV0(), tint);
            add(builder, pPoseStack, minScale, minScale, maxScale, sprite.getU1(), sprite.getV1(), tint);
            add(builder, pPoseStack, maxScale, minScale, maxScale, sprite.getU0(), sprite.getV1(), tint);

            add(builder, pPoseStack, maxScale, minScale, minScale, sprite.getU0(), sprite.getV1(), tint);
            add(builder, pPoseStack, minScale, minScale, minScale, sprite.getU1(), sprite.getV1(), tint);
            add(builder, pPoseStack, minScale, height, minScale, sprite.getU1(), sprite.getV0(), tint);
            add(builder, pPoseStack, maxScale, height, minScale, sprite.getU0(), sprite.getV0(), tint);
            pPoseStack.popPose();
        }
    }

    public static void directionCorrection(PoseStack poseStack, Direction direction) {
        if (direction.get2DDataValue() != -1) poseStack.mulPose(Axis.YP.rotationDegrees(90 * (direction.get2DDataValue() - 2)));
        else poseStack.mulPose(Axis.XP.rotationDegrees(-90 * direction.getAxisDirection().getStep()));
        switch (direction) {
            case SOUTH -> poseStack.translate(-1f, 0, -1f);
            case EAST -> poseStack.translate(-1f, 0, 0);
            case WEST -> poseStack.translate(0, 0, -1f);
            case UP -> poseStack.translate(0, -1f, 0f);
            case DOWN -> poseStack.translate(0f, 0, -1f);
        }
    }

    public static void renderTiledFluid(PoseStack pPoseStack, MultiBufferSource pBufferSource, Fluid fluid, PipeConnectionTypes type, Direction direction) {
        if (!fluid.isSame(Fluids.EMPTY)) {
            IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(fluid);
            ResourceLocation fluidFlowing = extension.getFlowingTexture();
            int tint = extension.getTintColor(new FluidStack(fluid, 1));
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidFlowing);
            VertexConsumer builder = pBufferSource.getBuffer(RenderType.translucent());

            if (type == PipeConnectionTypes.NONE) {
                createSquaredFace(builder, pPoseStack, 0.375f, 0.625f, 0.3001f, 0.6999f, sprite, tint, direction);
            } else if (type == PipeConnectionTypes.CABLE) {
                for (Direction faceRotation : getFaceRotation(direction)) {
                    if (direction.getAxis() == Direction.Axis.X) {
                        if (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
                            createRectangularFace(builder, pPoseStack, 0f, 0.375f, 0.375f, 0.625f, 0.3001f, 0.6999f, sprite, tint, faceRotation);
                        else createRectangularFace(builder, pPoseStack, 0.625f, 1f, 0.375f, 0.625f, 0.3001f, 0.6999f, sprite, tint, faceRotation);
                    } else if (direction.getAxis() == Direction.Axis.Z) {
                        if (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
                            createRectangularFace(builder, pPoseStack, 0.375f, 0.625f, 0f, 0.375f, 0.3001f, 0.6999f, sprite, tint, faceRotation);
                        else createRectangularFace(builder, pPoseStack, 0.375f, 0.625f, 0.625f, 1f, 0.3001f, 0.6999f, sprite, tint, faceRotation);
                    } else {
                        if (faceRotation.getAxis() == Direction.Axis.X) {
                            if (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
                                createRectangularFace(builder, pPoseStack, 0f, 0.375f, 0.375f, 0.625f, 0.3001f, 0.6999f, sprite, tint, faceRotation);
                            else createRectangularFace(builder, pPoseStack, 0.625f, 1f, 0.375f, 0.625f, 0.3001f, 0.6999f, sprite, tint, faceRotation);
                        }
                        if (faceRotation.getAxis() == Direction.Axis.Z) {
                            if (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
                                createRectangularFace(builder, pPoseStack, 0.375f, 0.625f, 0f, 0.375f, 0.3001f, 0.6999f, sprite, tint, faceRotation);
                            else createRectangularFace(builder, pPoseStack, 0.375f, 0.625f, 0.625f, 1f, 0.3001f, 0.6999f, sprite, tint, faceRotation);
                        }
                    }
                }
            }
        }
    }

    private static void createSquaredFace(VertexConsumer builder, PoseStack poseStack, float min, float max, float posMin, float posMax, TextureAtlasSprite sprite, int tint, Direction rotation) {
        switch (rotation) {
            case DOWN -> createFace(builder, poseStack, v(min, posMin, max), v(min, posMin, min), v(max, posMin, min), v(max, posMin, max), sprite, tint); // D;
            case UP -> createFace(builder, poseStack, v(max, posMax, max), v(max, posMax, min), v(min, posMax, min), v(min, posMax, max), sprite, tint); // U;
            case NORTH -> createFace(builder, poseStack, v(max, min, posMin), v(min, min, posMin), v(min, max, posMin), v(max, max, posMin), sprite, tint); // N;
            case SOUTH -> createFace(builder, poseStack, v(max, max, posMax), v(min, max, posMax), v(min, min, posMax), v(max, min, posMax), sprite, tint); // S;
            case WEST -> createFace(builder, poseStack, v(posMin, max, max), v(posMin, max, min), v(posMin, min, min), v(posMin, min, max), sprite, tint); // W;
            case EAST -> createFace(builder, poseStack, v(posMax, min, max), v(posMax, min, min), v(posMax, max, min), v(posMax, max, max), sprite, tint); // E;
        }
    }

    private static void createRectangularFace(VertexConsumer builder, PoseStack poseStack, float minA, float maxA, float minB, float maxB, float minC, float maxC, TextureAtlasSprite sprite, int tint, Direction rotation) {
        switch (rotation) {
            case DOWN -> createFace(builder, poseStack, v(minA, minC, maxB), v(minA, minC, minB), v(maxA, minC, minB), v(maxA, minC, maxB), sprite, tint); // D;
            case UP -> createFace(builder, poseStack, v(maxA, maxC, maxB), v(maxA, maxC, minB), v(minA, maxC, minB), v(minA, maxC, maxB), sprite, tint); // U;
            case NORTH -> createFace(builder, poseStack,
                    v(maxA, minB, minC),
                    v(minA, minB, minC),
                    v(minA, maxB, minC),
                    v(maxA, maxB, minC), sprite, tint); // N;
            case SOUTH -> createFace(builder, poseStack, v(maxA, maxB, maxC), v(minA, maxB, maxC), v(minA, minB, maxC), v(maxA, minB, maxC), sprite, tint); // S;
            case WEST -> createFace(builder, poseStack, v(minC, maxA, maxB), v(minC, maxA, minB), v(minC, minA, minB), v(minC, minA, maxB), sprite, tint); // W;
            case EAST -> createFace(builder, poseStack,
                    v(maxC, minA, maxB),
                    v(maxC, minA, minB),
                    v(maxC, maxA, minB),
                    v(maxC, maxA, maxB), sprite, tint); // E;
        }
    }

    private static void createFace(VertexConsumer builder, PoseStack poseStack, Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int tint) {
        add(builder, poseStack, v1.x, v1.y, v1.z, sprite.getU0(), sprite.getV0(), tint);
        add(builder, poseStack, v2.x, v2.y, v2.z, sprite.getU1(), sprite.getV0(), tint);
        add(builder, poseStack, v3.x, v3.y, v3.z, sprite.getU1(), sprite.getV1(), tint);
        add(builder, poseStack, v4.x, v4.y, v4.z, sprite.getU0(), sprite.getV1(), tint);
    }

    private static Direction[] getFaceRotation(Direction direction) {
        return switch (direction.getAxis()) {
            case X -> new Direction[] {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};
            case Y -> new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
            case Z -> new Direction[] {Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST};
        };
    }

    private static void add(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float u, float v, int tint) {
        renderer.addVertex(stack.last().pose(), x, y, z).setColor(tint).setUv(u, v).setUv2(0, 200).setNormal(1, 0, 0); // pV: Brightness
    }

    private static void add(VertexConsumer renderer, PoseStack stack, double x, double y, double z, float u, float v, int tint) {
        renderer.addVertex(stack.last().pose(), (float) x, (float) y, (float) z).setColor(tint).setUv(u, v).setUv2(0, 200).setNormal(1, 0, 0); // pV: Brightness
    }

    private static Vec3 v(float x, float y, float z) {
        return new Vec3(x, y, z);
    }
}
