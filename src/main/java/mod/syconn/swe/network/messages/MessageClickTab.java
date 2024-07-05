package mod.syconn.swe.network.messages;

import io.netty.buffer.ByteBuf;
import mod.syconn.swe.Main;
import mod.syconn.swe.blockentities.PipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MessageClickTab(BlockPos pos, Direction direction) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MessageClickTab> TYPE = new CustomPacketPayload.Type<>(Main.loc("click_tab"));
    public static final StreamCodec<ByteBuf, MessageClickTab> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, MessageClickTab::pos, Direction.STREAM_CODEC, MessageClickTab::direction, MessageClickTab::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MessageClickTab message, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player.level().getBlockEntity(message.pos) instanceof PipeBlockEntity pe) {
                pe.setTarget(message.d);
                NetworkHooks.openScreen(player, pe, message.pos);
            }
        });
    }
}
