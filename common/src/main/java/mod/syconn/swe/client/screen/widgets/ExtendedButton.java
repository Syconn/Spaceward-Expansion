/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package mod.syconn.swe.client.screen.widgets;

import mod.syconn.swe.util.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public class ExtendedButton extends Button implements GuiHelper {

    public ExtendedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler) {
        this(xPos, yPos, width, height, displayString, handler, DEFAULT_NARRATION);
    }

    public ExtendedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler, CreateNarration createNarration) {
        super(xPos, yPos, width, height, displayString, handler, createNarration);
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        Minecraft mc = Minecraft.getInstance();
        int k = !this.active ? 0 : (this.isHoveredOrFocused() ? 2 : 1);
        blitWithBorder(guiGraphics, WIDGETS_LOCATION, this.getX(), this.getY(), 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2);

        final FormattedText buttonText = ellipsis(this.getMessage(), this.width - 6); // Remove 6 pixels so that the text is always contained within the button's borders
        guiGraphics.drawCenteredString(mc.font, Language.getInstance().getVisualOrder(buttonText), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, getFGColor(active));
    }

    public Font font() {
        return Minecraft.getInstance().font;
    }
}
