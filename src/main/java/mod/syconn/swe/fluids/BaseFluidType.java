package mod.syconn.swe.fluids;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swe.Main;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BaseFluidType extends FluidType {

    public static final ResourceLocation O2_STILL_RL = ResourceLocation.fromNamespaceAndPath(Main.MODID, "block/o2_still");
    public static final ResourceLocation O2_FLOWING_RL = ResourceLocation.fromNamespaceAndPath(Main.MODID,"block/o2_flowing");
    public static final ResourceLocation O2_OVERLAY_RL = ResourceLocation.fromNamespaceAndPath(Main.MODID, "block/o2_overlay.png");

    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    private final int tintColor;
    private final Vector3f fogColor;

    public BaseFluidType(final ResourceLocation stillTexture, final ResourceLocation flowingTexture, final ResourceLocation overlayTexture, final int tintColor, final Vector3f fogColor, final FluidType.Properties properties) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.tintColor = tintColor;
        this.fogColor = fogColor;
    }

    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            public @Nullable ResourceLocation getOverlayTexture() {
                return overlayTexture;
            }

            public int getTintColor() {
                return tintColor != -1 ? tintColor : 0xFFFFFFFF;
            }

            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor;
            }

            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f);
            }
        });
    }
}
