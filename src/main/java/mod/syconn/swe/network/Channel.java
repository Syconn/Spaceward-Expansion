package mod.syconn.swe.network;

import mod.syconn.api.client.packets.ClientBoundUpdatePipeCache;
import mod.syconn.api.world.packets.ServerBoundInteractableButtonPress;
import mod.syconn.swe.network.messages.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Channel {

    public static void onRegisterPayloadHandler(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ServerBoundToggleDisperser.TYPE, ServerBoundToggleDisperser.STREAM_CODEC, ServerBoundToggleDisperser::handle);
        registrar.playToServer(ServerBoundInteractableButtonPress.TYPE, ServerBoundInteractableButtonPress.STREAM_CODEC, ServerBoundInteractableButtonPress::handle);

        registrar.playToClient(ClientBoundUpdatePlanetSettings.TYPE, ClientBoundUpdatePlanetSettings.STREAM_CODEC, ClientBoundUpdatePlanetSettings::handle);
        registrar.playToClient(ClientBoundUpdatePipeCache.TYPE, ClientBoundUpdatePipeCache.STREAM_CODEC, ClientBoundUpdatePipeCache::handle);

        registrar.playBidirectional(BiBoundUpdateSpaceSuit.TYPE, BiBoundUpdateSpaceSuit.STREAM_CODEC, BiBoundUpdateSpaceSuit::handle);
    }

    public static <MSG extends CustomPacketPayload> void sendToServer(MSG message) {
        PacketDistributor.sendToServer(message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayer(MSG message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }
}
