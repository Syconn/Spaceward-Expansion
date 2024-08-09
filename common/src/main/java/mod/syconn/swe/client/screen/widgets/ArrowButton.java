package mod.syconn.swe.client.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class ArrowButton extends ExtendedButton {

    private final Type type;

    public ArrowButton(int xPos, int yPos, Type type, OnPress handler) {
        super(xPos, yPos, 11, 7, Component.empty(), handler);
        this.type = type;
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (!isHovered()) {
            if (type == Type.DOWN) guiGraphics.blit(Main.loc("textures/gui/fluid_pipe.png"), getX(), getY(), 176, 0, width, height);
            else guiGraphics.blit(Main.loc("textures/gui/fluid_pipe.png"), getX(), getY(), 187, 0, width, height);
        } else {
            if (type == Type.DOWN) guiGraphics.blit(Main.loc("textures/gui/fluid_pipe.png"), getX(), getY(), 198, 0, width, height);
            else guiGraphics.blit(Main.loc("textures/gui/fluid_pipe.png"), getX(), getY(), 209, 0, width, height);
        }
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        UP,
        DOWN
    }
}
