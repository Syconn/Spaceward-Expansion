package mod.syconn.api.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class SpriteButton extends ExtendedButton {

    private ResourceLocation sprite;
    private Component hoverInfo;
    private int xLoc, yLoc;

    public SpriteButton(int xPos, int yPos, int width, int height, Component displayString, Component hoverInfo, ResourceLocation sprite, int xLoc, int yLoc, OnPress handler) {
        super(xPos, yPos, width, height, displayString, handler);
        this.hoverInfo = hoverInfo;
        this.sprite = sprite;
        this.xLoc = xLoc;
        this.yLoc = yLoc;
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (visible) {
            if (isHoveredOrFocused() && !hoverInfo.equals(Component.empty())) guiGraphics.renderTooltip(Minecraft.getInstance().font, hoverInfo, mouseX, mouseY);
            guiGraphics.blit(sprite, getX(), getY(), xLoc, yLoc, width, height);
            if (!getMessage().equals(Component.empty())) guiGraphics.drawString(Minecraft.getInstance().font, getMessage(), getX() + width / 2 - 3, getY() + height / 2 - 4, DyeColor.BLACK.getFireworkColor());
        }
    }

    public void setInteractable(boolean interactable) {
        visible = interactable;
        active = interactable;
    }

    public void setHoverInfo(Component hoverInfo) {
        this.hoverInfo = hoverInfo;
    }

    public void setSprite(int xLoc, int yLoc) {
        this.xLoc = xLoc;
        this.yLoc = yLoc;
    }

    public void setSprite(ResourceLocation sprite, int xLoc, int yLoc) {
        this.sprite = sprite;
        this.xLoc = xLoc;
        this.yLoc = yLoc;
    }
}
