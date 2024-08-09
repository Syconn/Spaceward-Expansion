package mod.syconn.swe.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerEvents {

    public static Event<PlayerTickCallback> PLAYER_TICK = EventFactory.createArrayBacked(PlayerTickCallback.class, listeners -> (player) -> {
        for (PlayerTickCallback callback : listeners) callback.tick(player);
    });

    public static Event<PlayerJoinCallback> PLAYER_JOIN = EventFactory.createArrayBacked(PlayerJoinCallback.class, listeners -> (player) -> {
        for (PlayerJoinCallback callback : listeners) callback.join(player);
    });

    public static Event<PlayerDisconnectCallback> PLAYER_DISCONNECT = EventFactory.createArrayBacked(PlayerDisconnectCallback.class, listeners -> (player) -> {
        for (PlayerDisconnectCallback callback : listeners) callback.disconnect(player);
    });

    public interface PlayerTickCallback {
        void tick(Player player);
    }

    public interface PlayerJoinCallback {
        void join(ServerPlayer player);
    }

    public interface PlayerDisconnectCallback {
        void disconnect(ServerPlayer player);
    }
}
