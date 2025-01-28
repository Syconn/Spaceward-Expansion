package mod.syconn.swe.util;

import mod.syconn.swe.network.Network;
import mod.syconn.swe.network.messages.MessageSyncPersistentData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public interface PersistentData {

    CompoundTag getPersistentData();
    void syncPersistentData(CompoundTag tag);
    void updatePersistentData(Player player, Function<CompoundTag, CompoundTag> function);

    default void sync(PersistentData data, Player player) {
        if (player instanceof ServerPlayer sp ) Network.CHANNEL.sendToPlayer(sp, new MessageSyncPersistentData(data.getPersistentData()));
        else Network.CHANNEL.sendToServer(new MessageSyncPersistentData(data.getPersistentData()));
    }
}
