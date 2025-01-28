package mod.syconn.swe.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.swe.Constants;
import mod.syconn.swe.network.messages.MessageSyncPersistentData;
import mod.syconn.swe.network.messages.MessageUpdateClientPipeCache;
import mod.syconn.swe.network.messages.MessageUpdatePipeState;

public class Network {
    public static NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void initC2S() {
        CHANNEL.register(MessageSyncPersistentData.class, MessageSyncPersistentData::encode, MessageSyncPersistentData::new, MessageSyncPersistentData::apply);
        CHANNEL.register(MessageUpdatePipeState.class, MessageUpdatePipeState::encode, MessageUpdatePipeState::new, MessageUpdatePipeState::apply);
    }

    public static void initS2C() {
        CHANNEL.register(MessageSyncPersistentData.class, MessageSyncPersistentData::encode, MessageSyncPersistentData::new, MessageSyncPersistentData::apply);
        CHANNEL.register(MessageUpdateClientPipeCache.class, MessageUpdateClientPipeCache::encode, MessageUpdateClientPipeCache::new, MessageUpdateClientPipeCache::apply);
    }
}
