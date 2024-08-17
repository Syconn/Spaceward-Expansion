package mod.syconn.swe.mixin;

import mod.syconn.swe.events.PlayerEvents;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class FabricPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        PlayerEvents.PLAYER_TICK.invoker().tick((Player) (Object) this);
    }
}
