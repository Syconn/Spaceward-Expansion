package mod.syconn.swe.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swe.items.SpaceArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;

public class ClientHooks {

    public static void overridePlayerScreen(GuiGraphics guiGraphics, AbstractContainerScreen<?> screen) {
        if(screen instanceof InventoryScreen inventory) {
            Player p = Minecraft.getInstance().player;
            if (p != null && SpaceArmor.hasFullKit(p)) {
                int left = inventory.getGuiLeft();
                int top = inventory.getGuiTop();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                guiGraphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, left + 76, top + 43, 7, 7, 18, 18, 256, 256);
                guiGraphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, left + 76, top + 25, 7, 7, 18, 18, 256, 256);
            }
        }
    }
}
