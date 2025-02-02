package mod.syconn.swe.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.swe.Constants;
import mod.syconn.swe.network.messages.*;

public class Network {
    public static NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void init() {
        CHANNEL.register(MessageSyncPersistentData.class, MessageSyncPersistentData::encode, MessageSyncPersistentData::new, MessageSyncPersistentData::apply);
        CHANNEL.register(MessageUpdatePipeState.class, MessageUpdatePipeState::encode, MessageUpdatePipeState::new, MessageUpdatePipeState::apply);
        CHANNEL.register(MessageSyncPersistentData.class, MessageSyncPersistentData::encode, MessageSyncPersistentData::new, MessageSyncPersistentData::apply);
        CHANNEL.register(MessageUpdateClientPipeCache.class, MessageUpdateClientPipeCache::encode, MessageUpdateClientPipeCache::new, MessageUpdateClientPipeCache::apply);
        CHANNEL.register(MessageSyncFluidBlock.class, MessageSyncFluidBlock::encode, MessageSyncFluidBlock::new, MessageSyncFluidBlock::apply);
        CHANNEL.register(MessageChangeInteractionSide.class, MessageChangeInteractionSide::encode, MessageChangeInteractionSide::new, MessageChangeInteractionSide::apply);
    }
}
