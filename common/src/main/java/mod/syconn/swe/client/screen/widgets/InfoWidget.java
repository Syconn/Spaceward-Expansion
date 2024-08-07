package mod.syconn.swe.client.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swe.Main;
import mod.syconn.swe.util.BlockInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class InfoWidget extends AbstractWidget {

    private static final ResourceLocation TEXTURE = Main.loc("textures/gui/elements.png");
    private final List<Component> text = new ArrayList<>();
    private final BlockEntity be;

    public InfoWidget(int x, int y, BlockEntity be) {
        super(x, y, 20, 20, Component.literal("Information Button"));
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

    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        createText();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (isHoveredOrFocused()) {
            pGuiGraphics.blit(TEXTURE, getX(), getY(), 20, 0, 20, 20);
            pGuiGraphics.renderComponentTooltip(Minecraft.getInstance().font, text, pMouseX, pMouseY);
        }
        else pGuiGraphics.blit(TEXTURE, getX(), getY(), 0, 0, 20, 20);
    }

    public void setFocused(boolean pFocused) {
        super.setFocused(false);
    }

    protected void updateWidgetNarration(NarrationElementOutput p_259858_) { this.defaultButtonNarrationText(p_259858_); }
}