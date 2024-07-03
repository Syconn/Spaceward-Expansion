package mod.syconn.swe.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import mod.syconn.swe.Main;

public class ArrowButton extends ExtendedButton {

    private final Type type;

    public ArrowButton(int xPos, int yPos, Type type, OnPress handler) {
        super(xPos, yPos, 11, 7, Component.empty(), handler);
        this.type = type;
    }

    @Override
    public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, new ResourceLocation(Main.MODID, "textures/gui/fluid_pipe.png"));
        if (!isHovered()) {
            if (type == Type.DOWN) blit(poseStack, getX(), getY(), 176, 0, width, height);
            else blit(poseStack, getX(), getY(), 187, 0, width, height);
        } else {
            if (type == Type.DOWN) blit(poseStack, getX(), getY(), 198, 0, width, height);
            else blit(poseStack, getX(), getY(), 209, 0, width, height);
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
