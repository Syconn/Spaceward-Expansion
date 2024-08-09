package mod.syconn.swe.mixin;

import mod.syconn.swe.events.PlayerEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(method = "remove", at = @At("HEAD"))
    public void disconnect(ServerPlayer player, CallbackInfo ci) {
        PlayerEvents.PLAYER_DISCONNECT.invoker().disconnect(player);
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void connect(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        PlayerEvents.PLAYER_JOIN.invoker().join(player);
    }
}
