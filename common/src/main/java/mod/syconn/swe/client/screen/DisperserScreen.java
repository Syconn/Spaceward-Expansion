package mod.syconn.swe.client.screen;

import mod.syconn.swe.Constants;
import mod.syconn.swe.client.screen.widgets.ExtendedButton;
import mod.syconn.swe.server.container.DisperserMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DisperserScreen extends AbstractContainerScreen<DisperserMenu> {

    private static final ResourceLocation BG = Constants.withId("textures/gui/disperser.png");

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
//        Network.sendToServer(new ServerBoundToggleDisperser(menu.getBE().getBlockPos()));
    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) { }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(BG, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        FluidTank tank = menu.getBE().getFluidTank();
//        if (!tank.isEmpty()) {
//            FluidHolder fluidHolder = menu.getBE().getFluidTank().getFluid();
//            int i = Services.FLUID_EXTENSIONS.getTintColor(fluidHolder);
//            int u = (int) ((double) (menu.getBE().getFluidTank().getFluid().getAmount()) / menu.getBE().getFluidTank().getCapacity() * 70);
//            RenderSystem.setShaderColor((float) (i >> 16 & 255) / 255.0F, (float) (i >> 8 & 255) / 255.0F, (float) (i & 255) / 255.0F, 255.0F);
//            pGuiGraphics.blit(, leftPos + 10, topPos + 8 + (70 - u), 0, 70, 34, u);
//            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            pGuiGraphics.blit(BG, leftPos + 10, topPos + 8, 176, 0, 6, 70);
//            Component infoComponent = Component.literal(tank.getFluid().getAmount() + "mb/" + tank.getCapacity() + "mb").withStyle(ChatFormatting.GRAY);
//            if (leftPos + 10 <= pMouseX && pMouseX <= leftPos + 43 && topPos + 8 <= pMouseY && pMouseY <= topPos + 77 && !fluidHolder.is(Fluids.EMPTY))
//                pGuiGraphics.renderComponentTooltip(font, List.of(Services.FLUID_EXTENSIONS.getTooltip(fluidHolder).getFirst(), infoComponent), pMouseX, pMouseY);
//        }
    }
}