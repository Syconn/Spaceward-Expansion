package mod.syconn.swe.network;

import mod.syconn.swe.network.messages.MessageChange;
import mod.syconn.swe.network.messages.MessageClickArrow;
import mod.syconn.swe.network.messages.MessageClickTab;
import mod.syconn.swe.network.messages.MessageToggleDisperser;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Channel {

    public static void onRegisterPayloadHandler(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(MessageChange.TYPE, MessageChange.STREAM_CODEC, MessageChange::handle);
        registrar.playToServer(MessageClickTab.TYPE, MessageClickTab.STREAM_CODEC, MessageClickTab::handle);
        registrar.playToServer(MessageClickArrow.TYPE, MessageClickArrow.STREAM_CODEC, MessageClickArrow::handle);
        registrar.playToServer(MessageToggleDisperser.TYPE, MessageToggleDisperser.STREAM_CODEC, MessageToggleDisperser::handle);
    }

    public static <MSG extends CustomPacketPayload> void sendToServer(MSG message) {
        PacketDistributor.sendToServer(message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayer(MSG message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }
}
