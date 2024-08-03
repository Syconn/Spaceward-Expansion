package mod.syconn.swe2.api.world.packets;

import io.netty.buffer.ByteBuf;
import mod.syconn.swe2.api.Constants;
import mod.syconn.swe2.api.blockEntity.BaseFluidPipeBE;
import mod.syconn.swe2.api.util.PipeConnectionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerBoundUpdatePipeState(BlockPos pos, Direction side, PipeConnectionTypes connection) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerBoundUpdatePipeState> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.ID, "update_pipe_state"));
    public static final StreamCodec<ByteBuf, ServerBoundUpdatePipeState> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ServerBoundUpdatePipeState::pos, Direction.STREAM_CODEC, ServerBoundUpdatePipeState::side, PipeConnectionTypes.STREAM_CODEC, ServerBoundUpdatePipeState::connection, ServerBoundUpdatePipeState::new);
    public Type<ServerBoundUpdatePipeState> type() { return TYPE; }

    public static void handle(ServerBoundUpdatePipeState message, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level().getBlockEntity(message.pos) instanceof BaseFluidPipeBE pipe) pipe.setConnectionType(message.side, message.connection);
        });
    }
}
