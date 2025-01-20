package mod.syconn.swe.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swe.blockentities.FluidPipeBE;
import mod.syconn.swe.client.screen.FluidPipeScreen;
import mod.syconn.swe.items.SpaceArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;

public class ClientHooks {

    public static void overrideAbstractScreen(GuiGraphics guiGraphics, AbstractContainerScreen<?> screen, int left, int top) {
        if(screen instanceof InventoryScreen) {
            Player p = Minecraft.getInstance().player;
            if (p != null && SpaceArmor.hasFullKit(p)) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                guiGraphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, left + 76, top + 43, 7, 7, 18, 18, 256, 256);
                guiGraphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, left + 76, top + 25, 7, 7, 18, 18, 256, 256);
            }
        }
    }

    public static void openPipeScreen(FluidPipeBE pipeBE) {
        Minecraft.getInstance().setScreen(new FluidPipeScreen(pipeBE));
    }
}
