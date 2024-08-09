package mod.syconn.swe.services;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.network.messages.Payload;
import mod.syconn.swe.platform.services.INetwork;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class NeoNetwork implements INetwork {

    private static PayloadRegistrar registrar;
    private static Map<Class<?>, Network.PlayMessage<?>> directory;

    public void sendToServer(Object payload) {
        PacketDistributor.sendToServer(encode(payload));
    }

    public void sendToClient(Object payload, ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer(serverPlayer, encode(payload));
    }

    public <T> void registerPlayS2C(Network.PlayMessage<T> message) {
        registrar.playToClient(message.type(), message.codec(), (payload, context) -> context.enqueueWork(() -> message.handler().accept(payload.msg(), context.player())));
    }

    public <T> void registerPlayC2S(Network.PlayMessage<T> message) {
        registrar.playToServer(message.type(), message.codec(), (payload, context) -> context.enqueueWork(() -> message.handler().accept(payload.msg(), context.player())));
    }

    public static void onRegisterPayloadHandler(final RegisterPayloadHandlersEvent event) {
        registrar = event.registrar("1");
        directory = createDirectory(Network.register);
        Network.S2CPayloads();
        Network.C2SPayloads();
    }

    @SuppressWarnings("unchecked")
    public <T> Payload<T> encode(T message) {
        Network.PlayMessage<T> msg = (Network.PlayMessage<T>) directory.get(message.getClass());
        if(msg == null) throw new IllegalArgumentException("Unregistered message: " + message.getClass().getName());
        return msg.getPayload(message);
    }

    private static Map<Class<?>, Network.PlayMessage<?>> createDirectory(Collection<Network.PlayMessage<?>> m) {
        Object2ObjectMap<Class<?>, Network.PlayMessage<?>> map = new Object2ObjectArrayMap<>();
        m.forEach(msg -> map.put(msg.msgClass(), msg));
        return Collections.unmodifiableMap(map);
    }
}
