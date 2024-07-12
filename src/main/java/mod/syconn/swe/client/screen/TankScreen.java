package mod.syconn.swe.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swe.Main;
import mod.syconn.swe.world.container.TankMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class TankScreen extends AbstractContainerScreen<TankMenu> {

    private static final ResourceLocation BG = Main.loc("textures/gui/tank.png");

    public TankScreen(TankMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) { }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        FluidTank tank = menu.getBE().getFluidTank();
        pGuiGraphics.blit(BG, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (!tank.isEmpty()) {
            FluidStack stack = tank.getFluidInTank(0);
            int i = IClientFluidTypeExtensions.of(stack.getFluid()).getTintColor(stack);
            int u = (int) ((double) (tank.getFluidAmount()) / tank.getCapacity() * 70);
            RenderSystem.setShaderColor((float)(i >> 16 & 255) / 255.0F, (float)(i >> 8 & 255) / 255.0F, (float)(i & 255) / 255.0F, 255.0F);
            pGuiGraphics.blit(menu.getBE().getGuiTexture(), leftPos + 34, topPos + 8 + (70 - u), 0, 70, 34, u);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            pGuiGraphics.blit(BG, leftPos + 34, topPos + 8, 176, 0, 6, 70);
            // TODO set fluid name to fluid color
            Component infoComponent = Component.literal(tank.getFluidAmount() + "mb/" + tank.getCapacity() + "mb").withStyle(ChatFormatting.GRAY);
            if (leftPos + 34 <= pMouseX && pMouseX <= leftPos + 67 && topPos + 8 <= pMouseY && pMouseY <= topPos + 77 && !stack.is(Fluids.EMPTY))
                pGuiGraphics.renderComponentTooltip(font, List.of(tank.getFluidInTank(0).getHoverName(), infoComponent), pMouseX, pMouseY);
        }
    }
}