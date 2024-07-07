package mod.syconn.swe.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.swe.network.Channel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import mod.syconn.swe.Main;
import mod.syconn.swe.client.screen.widgets.ArrowButton;
import mod.syconn.swe.client.screen.widgets.TabButton;
import mod.syconn.swe.world.container.PipeMenu;
import mod.syconn.swe.network.messages.MessageChange;
import mod.syconn.swe.network.messages.MessageClickArrow;
import mod.syconn.swe.network.messages.MessageClickTab;
import mod.syconn.swe.util.Helper;
import mod.syconn.swe.util.data.FluidPointSystem;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class PipeScreen extends AbstractContainerScreen<PipeMenu> {

    private final ResourceLocation BACKGROUND = Main.loc("textures/gui/fluid_pipe.png");

    private TabButton[] tabs;
    private ExtendedButton flipper;
    private final FluidPointSystem system = menu.getBE().getSystem();
    private FluidPointSystem.FluidPoint fluidPoint;

    public PipeScreen(PipeMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        fluidPoint = menu.getBE().selectedTab();
    }

    protected void init() {
        super.init();
        int sz = system.getPoints().size();
        tabs = new TabButton[sz];
        for (int i = 0; i < sz; i++) {
            TabButton.State state = i == 0 ? TabButton.State.LEFT : i == 5 ? TabButton.State.RIGHT : TabButton.State.MIDDLE;
            String name = system.getPoints().get(i).d().getName();
            tabs[i] = addRenderableWidget(new TabButton(leftPos + (28 + 5) * i, topPos, state, name, null, system.getPoints().get(i).equals(fluidPoint), this::tabClicked));
        }
        addRenderableWidget(new ArrowButton(this.leftPos + 29, this.topPos + 20, ArrowButton.Type.UP, this::arrowClick));
        addRenderableWidget(new ArrowButton(this.leftPos + 29, this.topPos + 60, ArrowButton.Type.DOWN, this::arrowClick));
        flipper = addRenderableWidget(new ExtendedButton(this.leftPos + 56, this.topPos + 33, 60, 20, Component.literal(fluidPoint.toString()), (b) -> Channel.sendToServer(new MessageChange(menu.getBE().getBlockPos()))));
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        fluidPoint = menu.getBE().selectedTab();
        flipper.setMessage(Component.literal(fluidPoint.toString()));
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (menu.getBE().selectedTab() != null) {
            pGuiGraphics.blit(BACKGROUND, this.leftPos + 151, this.topPos + 12, 176, 8, 18, 18);
            pGuiGraphics.blit(BACKGROUND, this.leftPos + 151, this.topPos + 34, 176, 8, 18, 18);
            pGuiGraphics.blit(BACKGROUND, this.leftPos + 151, this.topPos + 56, 176, 8, 18, 18);
            pGuiGraphics.blit(BACKGROUND, this.leftPos + 23, this.topPos + 31, 194, 8, 23, 24);
            pGuiGraphics.drawCenteredString(font, String.valueOf(fluidPoint.priority()), this.leftPos + 34, this.topPos + 39, DyeColor.WHITE.getTextColor());
        }
    }

    private void tabClicked(Button b) {
        if (b instanceof TabButton tab) {
            Channel.sendToServer(new MessageClickTab(menu.getBE().getBlockPos(), Helper.dirFromName(tab.getName())));
        }
    }

    private void arrowClick(Button b) {
        if (b instanceof ArrowButton tab) {
            Channel.sendToServer(new MessageClickArrow(menu.getBE().getBlockPos(), tab.getType() == ArrowButton.Type.UP ? 1 : -1));
        }
    }
}
