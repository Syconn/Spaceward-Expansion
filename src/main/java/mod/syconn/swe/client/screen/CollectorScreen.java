package mod.syconn.swe.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import mod.syconn.swe.Main;
import mod.syconn.swe.client.screen.widgets.InfoWidget;
import mod.syconn.swe.world.container.CollectorMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.List;

public class CollectorScreen extends AbstractContainerScreen<CollectorMenu> {

    private static final ResourceLocation BG = Main.loc("textures/gui/disperser.png");

    public CollectorScreen(CollectorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    protected void init() {
        super.init();
        addRenderableWidget(new InfoWidget(leftPos + 153, topPos + 3, menu.getBE()));
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(BG, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        FluidState state = menu.getBE().getFluidTank().getFluidInTank(0).getFluid().defaultFluidState();
        IClientFluidTypeExtensions.of(state).getStillTexture();
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