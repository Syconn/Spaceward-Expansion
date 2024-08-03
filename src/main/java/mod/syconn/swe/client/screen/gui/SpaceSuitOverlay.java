package mod.syconn.swe2.client.screen.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swe2.Registration;
import mod.syconn.swe2.world.data.attachments.SpaceSuit;
import mod.syconn.swe2.world.dimensions.PlanetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpaceSuitOverlay {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation AIR_SPRITE = ResourceLocation.withDefaultNamespace("hud/air");
    private static final ResourceLocation AIR_BURSTING_SPRITE = ResourceLocation.withDefaultNamespace("hud/air_bursting");

    public static LayeredDraw.Layer O2_OVERLAY = (pGuiGraphics, partialTicks) -> {
        Player player = (Player) minecraft.getCameraEntity();
        if (player != null && !minecraft.options.hideGui && minecraft.gameMode.canHurtPlayer() && displayOxygen(player) && player.hasData(Registration.SPACE_SUIT)) {
            int left = pGuiGraphics.guiWidth() / 2 + 91;
            minecraft.getProfiler().push("oxygen");
            SpaceSuit iSpaceSuit = player.getData(Registration.SPACE_SUIT);
            int max = iSpaceSuit.maxO2();
            int j2 = pGuiGraphics.guiHeight() - 49;
            int full = Mth.ceil((double) (Math.min(iSpaceSuit.O2(), max) - 2) * 10.0D / (double) iSpaceSuit.maxO2());
            int partial = Mth.ceil((double) Math.min(iSpaceSuit.O2(), max) * 10.0D / (double) iSpaceSuit.maxO2()) - full;
            RenderSystem.enableBlend();
            for (int x = 0; x < full + partial; x++) {
                if (x < full) pGuiGraphics.blitSprite(AIR_SPRITE, left - x * 8 - 9, j2, 9, 9);
                else pGuiGraphics.blitSprite(AIR_BURSTING_SPRITE, left - x * 8 - 9, j2, 9, 9);
            }
            RenderSystem.disableBlend();
            minecraft.getProfiler().pop();
        }
    };

    public static boolean displayOxygen(Player p){
        SpaceSuit suit = p.getData(Registration.SPACE_SUIT);
        if (suit.O2() >= suit.maxO2()) return false;
        return !PlanetManager.getSettings(p).breathable();
    }
}
