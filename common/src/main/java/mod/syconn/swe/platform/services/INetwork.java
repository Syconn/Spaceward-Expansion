package mod.syconn.swe.platform.services;

import mod.syconn.swe.network.Network;
import net.minecraft.server.level.ServerPlayer;

public interface INetwork {

    void sendToServer(Object payload);
    void sendToClient(Object payload, ServerPlayer serverPlayer);
    <T> void registerPlayS2C(Network.PlayMessage<T> message);
    <T> void registerPlayC2S(Network.PlayMessage<T> message);
    default <T> void registerClientHandler(Network.PlayMessage<T> message) {}
    default <T> void registerServerHandler(Network.PlayMessage<T> message) {}
}
