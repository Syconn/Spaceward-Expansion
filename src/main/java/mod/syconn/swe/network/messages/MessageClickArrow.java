package mod.syconn.swe.network.messages;

import io.netty.buffer.ByteBuf;
import mod.syconn.swe.Main;
import mod.syconn.swe.blockentities.PipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MessageClickArrow(BlockPos pos, int inc) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MessageClickArrow> TYPE = new CustomPacketPayload.Type<>(Main.loc("click_arrow"));
    public static final StreamCodec<ByteBuf, MessageClickArrow> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, MessageClickArrow::pos, ByteBufCodecs.VAR_INT, MessageClickArrow::inc, MessageClickArrow::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MessageClickArrow message, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player.level().getBlockEntity(message.pos) instanceof PipeBlockEntity pe) pe.increment(message.inc);
        });
    }
}
