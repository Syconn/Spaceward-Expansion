package mod.syconn.swe.network.messages;

import mod.syconn.swe.blockentities.FluidPipeBE;
import mod.syconn.swe.extra.PipePatterns;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public record ServerBoundUpdatePipeState(BlockPos pos, Direction side, PipePatterns.PipeConnectionTypes connection) {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerBoundUpdatePipeState> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ServerBoundUpdatePipeState::pos, Direction.STREAM_CODEC, ServerBoundUpdatePipeState::side, PipePatterns.PipeConnectionTypes.STREAM_CODEC, ServerBoundUpdatePipeState::connection, ServerBoundUpdatePipeState::new);

    public static void handle(ServerBoundUpdatePipeState message, Player player) {
        if (player.level().getBlockEntity(message.pos) instanceof FluidPipeBE pipe) pipe.setConnectionType(message.side, message.connection);
    }
}
