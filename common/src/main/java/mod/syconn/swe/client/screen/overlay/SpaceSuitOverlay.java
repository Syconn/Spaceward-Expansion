package mod.syconn.swe.client.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swe.common.data.SpacePlayerData;
import mod.syconn.swe.server.reloaders.PlanetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class SpaceSuitOverlay {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    public static void renderOverlay(GuiGraphics graphics, float tickDelta) {
        Player player = minecraft.player;
        if (player != null && !minecraft.options.hideGui && !player.isCreative() && displayOxygen(player)) {
            minecraft.getProfiler().push("oxygen");
            SpacePlayerData spaceData = SpacePlayerData.from(player);
            int left = graphics.guiWidth() / 2 + 91;
            int y = graphics.guiHeight() - 49;
            int max = spaceData.getMaxOxygen();
            int full = Mth.ceil((double) (Math.min(spaceData.getOxygen(), max) - 2) * 10.0D / (double) spaceData.getMaxOxygen());
            int partial = Mth.ceil((double) Math.min(spaceData.getOxygen(), max) * 10.0D / (double) spaceData.getMaxOxygen()) - full;
            RenderSystem.enableBlend();
            for (int x = 0; x < full + partial; x++) {
                if (x < full) graphics.blit(GUI_ICONS_LOCATION, left - x * 8 - 9, y, 16, 18, 9, 9);
                else graphics.blit(GUI_ICONS_LOCATION, left - x * 8 - 9, y, 25, 18, 9, 9);
            }
            RenderSystem.disableBlend();
            minecraft.getProfiler().pop();
        }
    }

    public static boolean displayOxygen(Player player){
        SpacePlayerData spaceData = SpacePlayerData.from(player);
        if (spaceData.getOxygen() >= spaceData.getMaxOxygen()) return false;
        return !PlanetManager.getSettings(player).breathable();
    }
}
