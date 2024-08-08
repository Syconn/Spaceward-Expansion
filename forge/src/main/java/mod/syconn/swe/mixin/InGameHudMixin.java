package mod.syconn.swe.mixin;

import mod.syconn.swe.client.screen.gui.SpaceSuitOverlay;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At(value = "TAIL"))
    public void render(GuiGraphics drawContext, DeltaTracker tickCounter, CallbackInfo callbackInfo) {
        SpaceSuitOverlay.O2_OVERLAY.render(drawContext, tickCounter);
    }
}
