package mod.syconn.swe.network.messages;

import io.netty.buffer.ByteBuf;
import mod.syconn.swe.Main;
import mod.syconn.swe.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MessageToggleDisperser(BlockPos pos) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MessageToggleDisperser> TYPE = new CustomPacketPayload.Type<>(Main.loc("toggle_disperser"));
    public static final StreamCodec<ByteBuf, MessageToggleDisperser> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, MessageToggleDisperser::pos, MessageToggleDisperser::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MessageToggleDisperser message, IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().level().getBlockEntity(message.pos, Registration.DISPERSER.get()).get().toggleEnabled();
        });
    }
}
