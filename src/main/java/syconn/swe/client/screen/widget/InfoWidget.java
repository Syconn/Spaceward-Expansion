package syconn.swe.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;
import org.joml.Vector2ic;
import syconn.swe.Main;
import syconn.swe.util.BlockInfo;

import java.util.ArrayList;
import java.util.List;

public class InfoWidget extends AbstractWidget {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/gui/elements.png");
    private final Screen screen;
    private List<Component> text = new ArrayList<>();
    private BlockEntity be;

    public InfoWidget(int x, int y, Screen screen, BlockEntity be) {
        super(x, y, 20, 20, Component.literal("Information Button"));
        this.screen = screen;
        this.be = be;
    }

    public void createText() {
        text.clear();
        if (be instanceof BlockInfo info && be instanceof MenuProvider provider) {
            text.add(provider.getDisplayName());
            text.add(Component.empty());
            text.add(Component.literal("Speed: " + info.getFluidRate() + " mb/s"));
        }
    }

    public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float p_268085_) {
        createText();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        if (isHoveredOrFocused()) {
            blit(poseStack, getX(), getY(), 20, 0, 20, 20);
            screen.renderComponentTooltip(poseStack, text, mouseX, mouseY);
        }
        else blit(poseStack, getX(), getY(), 0, 0, 20, 20);
    }

    protected void updateWidgetNarration(NarrationElementOutput p_259858_) { this.defaultButtonNarrationText(p_259858_); }
}
