package mod.syconn.swe.extra.platform.services;

import mod.syconn.swe.extra.core.IMenuData;
import mod.syconn.swe.network.Network;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

import java.util.OptionalInt;

public interface INetwork {

    <D extends IMenuData<D>> OptionalInt openMenuWithData(ServerPlayer player, MenuProvider provider, D data);

    void sendToServer(Object payload);
    void sendToClient(Object payload, ServerPlayer serverPlayer);
    <T> void registerPlayS2C(Network.PlayMessage<T> message);
    <T> void registerPlayC2S(Network.PlayMessage<T> message);
    <T> void registerPlayBiDirectional(Network.PlayMessage<T> message);
    default <T> void registerClientHandler(Network.PlayMessage<T> message) {}
    default <T> void registerServerHandler(Network.PlayMessage<T> message) {}
}
