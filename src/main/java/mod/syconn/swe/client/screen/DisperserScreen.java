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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import mod.syconn.swe.Main;
import mod.syconn.swe.client.screen.widgets.InfoWidget;
import mod.syconn.swe.world.container.DisperserMenu;
import mod.syconn.swe.network.messages.MessageToggleDisperser;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

import java.util.List;

public class DisperserScreen extends AbstractContainerScreen<DisperserMenu> {

    private static final ResourceLocation BG = Main.loc("textures/gui/disperser.png");

    public DisperserScreen(DisperserMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    protected void init() {
        super.init();
        addRenderableWidget(new ExtendedButton(leftPos + 91, topPos + 25, 60, 20, Component.literal(menu.getBE().isEnabled() ? "Enabled" : "Disabled"), this::onclick));
        addRenderableWidget(new InfoWidget(leftPos + 153, topPos + 3, menu.getBE()));
    }

    private void onclick(Button button) {
        button.setMessage(Component.literal(!menu.getBE().isEnabled() ? "Enabled" : "Disabled"));
        Channel.sendToServer(new MessageToggleDisperser(menu.getBE().getBlockPos()));
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(BG, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        FluidState state = menu.getBE().getFluidTank().getFluidInTank(0).getFluid().defaultFluidState();
        int i = IClientFluidTypeExtensions.of(state).getTintColor();
        int u = (int) ((double) (menu.getBE().getFluidTank().getFluidAmount()) / menu.getBE().getFluidTank().getCapacity() * 70);
        RenderSystem.setShaderColor((float)(i >> 16 & 255) / 255.0F, (float)(i >> 8 & 255) / 255.0F, (float)(i & 255) / 255.0F, 255.0F);
        pGuiGraphics.blit(menu.getBE().getGuiTexture(), leftPos + 10, topPos + 8 + (70 - u), 0, 70, 34, u);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.blit(BG, leftPos + 10, topPos + 8, 176, 0, 6, 70);
        if (leftPos + 10 <= pMouseX && pMouseX <= leftPos + 43 && topPos + 8 <= pMouseY && pMouseY <= topPos + 77 && !state.is(Fluids.EMPTY))
            pGuiGraphics.renderComponentTooltip(font, List.of(Component.literal(menu.getBE().getFluidTank().getFluidInTank(0).getHoverName().getString() + " " + menu.getBE().getFluidTank().getFluidAmount() + "mb")), pMouseX, pMouseY);
    }
}