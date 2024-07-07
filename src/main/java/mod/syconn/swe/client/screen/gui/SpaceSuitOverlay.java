package mod.syconn.swe.client.screen.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.swe.Registration;
import mod.syconn.swe.world.data.attachments.SpaceSuit;
import mod.syconn.swe.world.dimensions.DimSettingsManager;
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

    public static LayeredDraw.Layer O2_OVERLAY = (pGuiGraphics, partialTicks) -> {
        Player player = (Player) minecraft.getCameraEntity();
        if (player != null && !minecraft.options.hideGui && minecraft.gameMode.canHurtPlayer() && displayOxygen(player) && player.hasData(Registration.SPACE_SUIT)) {
            minecraft.getProfiler().push("o2");
            SpaceSuit iSpaceSuit = player.getData(Registration.SPACE_SUIT);
            RenderSystem.enableBlend();
            int left = pGuiGraphics.guiWidth() / 2 + 91;
            int top = pGuiGraphics.guiHeight() - 49;
            int air = iSpaceSuit.O2();
            int full = Mth.ceil((double) (air - 2) * 10.0D / (double) iSpaceSuit.maxO2());
            int partial = Mth.ceil((double) air * 10.0D / (double) iSpaceSuit.maxO2()) - full;
            for (int i = 0; i < full + partial; ++i) pGuiGraphics.blit(ResourceLocation.withDefaultNamespace("hud/air"), left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
            RenderSystem.disableBlend();
            minecraft.getProfiler().pop();
        }
    };

    public static boolean displayOxygen(Player p){
        SpaceSuit suit = p.getData(Registration.SPACE_SUIT);
        if (suit.O2() >= suit.maxO2()) return false;
        return !DimSettingsManager.getSettings(p).breathable();
    }
}
