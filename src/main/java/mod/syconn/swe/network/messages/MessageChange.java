package mod.syconn.swe.network.messages;

import io.netty.buffer.ByteBuf;
import mod.syconn.swe.Main;
import mod.syconn.swe.blockentities.PipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MessageChange(BlockPos pos) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MessageChange> TYPE = new CustomPacketPayload.Type<>(Main.loc("change"));
    public static final StreamCodec<ByteBuf, MessageChange> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, MessageChange::pos, MessageChange::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MessageChange message, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player.level().getBlockEntity(message.pos) instanceof PipeBlockEntity pe) pe.changeType();
        });
    }
}