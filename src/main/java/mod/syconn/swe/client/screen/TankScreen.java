package mod.syconn.swe.client.screen;

import mod.syconn.api.client.screen.InteractionSelectorScreen;
import mod.syconn.api.world.packets.ServerBoundInteractableButtonPress;
import mod.syconn.swe.Main;
import mod.syconn.swe.client.RenderUtil;
import mod.syconn.swe.network.Channel;
import mod.syconn.swe.world.container.TankMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class TankScreen extends InteractionSelectorScreen<TankMenu> {

    private static final ResourceLocation BG = Main.loc("textures/gui/tank.png");

    public TankScreen(TankMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, pMenu.getBE().getFluidTank());
    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) { }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        FluidTank tank = menu.getBE().getFluidTank();
        pGuiGraphics.blit(BG, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);

        FluidStack fluidStack = tank.getFluidInTank(0);
        IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        int u = (int) ((double) (tank.getFluidAmount()) / tank.getCapacity() * 70);
        if(fluidStack.isEmpty()) return;
        TextureAtlasSprite sprite = RenderUtil.getSprite(fluidStack);
        int tintColor = extension.getTintColor(fluidStack);
        float alpha = ((tintColor >> 24) & 0xFF) / 255f;
        float red = ((tintColor >> 16) & 0xFF) / 255f;
        float green = ((tintColor >> 8) & 0xFF) / 255f;
        float blue = (tintColor & 0xFF) / 255f;
        pGuiGraphics.setColor(red, green, blue, alpha);
        pGuiGraphics.blit(this.leftPos + 34, topPos + 8 + (70 - u), 0, 34, u, sprite);
        pGuiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        Component infoComponent = Component.literal(tank.getFluidAmount() + "mb/" + tank.getCapacity() + "mb").withStyle(ChatFormatting.GRAY);
        if (leftPos + 34 <= pMouseX && pMouseX <= leftPos + 67 && topPos + 8 <= pMouseY && pMouseY <= topPos + 77)
            pGuiGraphics.renderComponentTooltip(font, List.of(tank.getFluidInTank(0).getHoverName(), infoComponent), pMouseX, pMouseY);
    }

    protected int getMenuX() {
        return -85;
    }

    protected int getMenuY() {
        return 2;
    }

    protected int getSpriteX() {
        return leftPos - 27;
    }

    protected int getSpriteY() {
        return topPos + 6;
    }

    protected void sendPacket(Interactables interactable, Direction direction) {
        Channel.sendToServer(new ServerBoundInteractableButtonPress(menu.getBE().getBlockPos(), direction, interactable.getInteraction()));
    }
}