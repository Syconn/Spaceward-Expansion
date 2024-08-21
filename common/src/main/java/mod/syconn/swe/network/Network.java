package mod.syconn.swe.network;

import mod.syconn.swe.Constants;
import mod.syconn.swe.extra.core.IMenuData;
import mod.syconn.swe.extra.core.Payload;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.extra.platform.services.INetwork;
import mod.syconn.swe.network.messages.BiBoundUpdateSpaceSuit;
import mod.syconn.swe.network.messages.ClientBoundUpdatePipeCache;
import mod.syconn.swe.network.messages.ServerBoundInteractableButtonPress;
import mod.syconn.swe.network.messages.ServerBoundUpdatePipeState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

public class Network {

    private static final INetwork network = Services.NETWORK;
    public static final ArrayList<PlayMessage<?>> register = new ArrayList<>();

    public static void registerMessages() {
        register.clear(); // For Fabric Client Side Setup

        //Server Bound
        register.add(PlayMessage.of("update_interaction", ServerBoundInteractableButtonPress.class, ServerBoundInteractableButtonPress.STREAM_CODEC, ServerBoundInteractableButtonPress::handle, PacketFlow.SERVERBOUND));
        register.add(PlayMessage.of("update_pipe_state", ServerBoundUpdatePipeState.class, ServerBoundUpdatePipeState.STREAM_CODEC, ServerBoundUpdatePipeState::handle, PacketFlow.SERVERBOUND));

        //Client Bound
        register.add(PlayMessage.of("update_pipe_cache", ClientBoundUpdatePipeCache.class, ClientBoundUpdatePipeCache.STREAM_CODEC, ClientBoundUpdatePipeCache::handle, PacketFlow.CLIENTBOUND));

        // BiDirectional
        register.add(PlayMessage.of("update_space_suit", BiBoundUpdateSpaceSuit.class, BiBoundUpdateSpaceSuit.STREAM_CODEC, BiBoundUpdateSpaceSuit::handle, null));
    }

    public static void sendToServer(Object message) {
        network.sendToServer(message);
    }

    public static void sendToPlayer(Object message, ServerPlayer serverPlayer) {
        network.sendToClient(message, serverPlayer);
    }

    public static void C2SPayloads() {
        register.stream().filter(msg -> msg.clientBound() || msg.bothBound()).forEach(network::registerClientHandler);
    }

    public static void S2CPayloads() {
        register.stream().filter(PlayMessage::clientBound).forEach(network::registerPlayS2C);
        register.stream().filter(PlayMessage::serverBound).forEach(network::registerPlayC2S);
        register.stream().filter(PlayMessage::bothBound).forEach(network::registerPlayBiDirectional);
        register.stream().filter(msg -> msg.serverBound() || msg.bothBound()).forEach(network::registerServerHandler);
    }

    public static <D extends IMenuData<D>> OptionalInt openMenuWithData(ServerPlayer player, MenuProvider provider, D data) {
        return network.openMenuWithData(player, provider, data);
    }

    public record PlayMessage<T> (CustomPacketPayload.Type<Payload<T>> type, Class<T> msgClass, StreamCodec<RegistryFriendlyByteBuf, Payload<T>> codec, StreamCodec<RegistryFriendlyByteBuf, T> forgeCodec, BiConsumer<T, Player> handler, PacketFlow flow) {

        public static <T> PlayMessage<T> of(String id, Class<T> msgClass, StreamCodec<RegistryFriendlyByteBuf, T> forgeCodec, BiConsumer<T, Player> handler, PacketFlow flow) {
            CustomPacketPayload.Type<Payload<T>> payloadType = new CustomPacketPayload.Type<>(Constants.loc(id));
            return new PlayMessage<>(payloadType, msgClass, Payload.codec(payloadType, forgeCodec), forgeCodec, handler, flow);
        }

        public Payload<T> getPayload(T msg) { return new Payload<>(this.type, msg); }
        public boolean clientBound() { return this.flow == PacketFlow.CLIENTBOUND; }
        public boolean serverBound() { return this.flow == PacketFlow.SERVERBOUND; }
        public boolean bothBound() { return this.flow == null; }
    }
}
