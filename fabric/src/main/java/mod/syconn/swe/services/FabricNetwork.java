package mod.syconn.swe.services;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import mod.syconn.swe.extra.core.IMenuData;
import mod.syconn.swe.extra.core.Payload;
import mod.syconn.swe.extra.platform.services.INetwork;
import mod.syconn.swe.network.Network;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.OptionalInt;

public class FabricNetwork implements INetwork {

    public static Map<Class<?>, Network.PlayMessage<?>> directory;

    public <D extends IMenuData<D>> OptionalInt openMenuWithData(ServerPlayer player, MenuProvider provider, D data) {
        return player.openMenu(new ExtendedScreenHandlerFactory<D>() {
            public D getScreenOpeningData(ServerPlayer player) {
                return data;
            }

            public Component getDisplayName() {
                return provider.getDisplayName();
            }

            public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
                return provider.createMenu(windowId, playerInventory, player);
            }
        });
    }

    public <T> void registerPlayBiDirectional(Network.PlayMessage<T> message) {}

    public void sendToServer(Object payload) {
        ClientPlayNetworking.send(encode(payload));
    }

    public void sendToClient(Object payload, ServerPlayer serverPlayer) {
        ServerPlayNetworking.send(serverPlayer, encode(payload));
    }

    public <T> void registerPlayS2C(Network.PlayMessage<T> message) {
        createDirectory();
        PayloadTypeRegistry.playS2C().register(message.type(), message.codec());
    }

    public <T> void registerPlayC2S(Network.PlayMessage<T> message) {
        createDirectory();
        PayloadTypeRegistry.playC2S().register(message.type(), message.codec());
    }

    public <T> void registerClientHandler(Network.PlayMessage<T> message) {
        ClientPlayNetworking.registerGlobalReceiver(message.type(), (payload, context) -> context.client().execute(() -> message.handler().accept(payload.msg(), context.player())));
    }

    public <T> void registerServerHandler(Network.PlayMessage<T> message) {
        ServerPlayNetworking.registerGlobalReceiver(message.type(), (payload, context) -> context.server().execute(() -> message.handler().accept(payload.msg(), context.player())));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private <T> Payload<T> encode(Object message) {
        Network.PlayMessage msg = directory.get(message.getClass());
        if(msg == null) throw new IllegalArgumentException("Unregistered message: " + message.getClass().getName());
        return msg.getPayload(message);
    }

    private static void createDirectory() {
        Object2ObjectMap<Class<?>, Network.PlayMessage<?>> map = new Object2ObjectArrayMap<>();
        ((Collection<Network.PlayMessage<?>>) Network.register).forEach(msg -> map.put(msg.msgClass(), msg));
        directory = Collections.unmodifiableMap(map);
    }
}
