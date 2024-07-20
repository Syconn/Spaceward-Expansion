package mod.syconn.swe.api.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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
