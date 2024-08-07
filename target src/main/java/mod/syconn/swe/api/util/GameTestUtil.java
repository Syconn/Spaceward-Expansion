package mod.syconn.api.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;

import java.util.UUID;

public class GameTestUtil {

    public static AbstractClientPlayer makeMockPlayer(ClientLevel level) {
        return new AbstractClientPlayer(level, new GameProfile(UUID.randomUUID(), "test-mock-player")) {
            /**
             * Returns true if the player is in spectator mode.
             */
            public boolean isSpectator() {
                return false;
            }

            public boolean isCreative() {
                return true;
            }
        };
    }
}
