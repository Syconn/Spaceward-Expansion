package mod.syconn.swe.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swe.Main;
import mod.syconn.swe.world.container.TankMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.List;

public class TankScreen extends AbstractContainerScreen<TankMenu> {

    private static final ResourceLocation BG = Main.loc("textures/gui/tank.png");

    public TankScreen(TankMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
        pGuiGraphics.drawString(font, menu.getBE().getFluidTank().getFluidAmount() + "mb", this.titleLabelX + 90, this.titleLabelY, 4210752);
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
        pGuiGraphics.blit(menu.getBE().getGuiTexture(), leftPos + 34, topPos + 8 + (70 - u), 0, 70, 34, u);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.blit(BG, leftPos + 34, topPos + 8, 176, 0, 6, 70);
        if (leftPos + 34 <= pMouseX && pMouseX <= leftPos + 67 && topPos + 8 <= pMouseY && pMouseY <= topPos + 77 && !state.is(Fluids.EMPTY))
            pGuiGraphics.renderComponentTooltip(font, List.of(menu.getBE().getFluidTank().getFluidInTank(0).getHoverName()), pMouseX, pMouseY);
    }
}