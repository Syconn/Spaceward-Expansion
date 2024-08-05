package mod.syconn.swe2.network.messages;

import io.netty.buffer.ByteBuf;
import mod.syconn.swe2.Main;
import mod.syconn.swe2.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerBoundToggleDisperser(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ServerBoundToggleDisperser> TYPE = new Type<>(Main.loc("toggle_disperser"));
    public static final StreamCodec<ByteBuf, ServerBoundToggleDisperser> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ServerBoundToggleDisperser::pos, ServerBoundToggleDisperser::new);

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerBoundToggleDisperser message, IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().level().getBlockEntity(message.pos, Registration.DISPERSER.get()).get().toggleEnabled();
        });
    }
}
