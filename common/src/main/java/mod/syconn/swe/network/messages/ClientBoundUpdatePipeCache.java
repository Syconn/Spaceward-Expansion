package mod.syconn.swe.network.messages;

import mod.syconn.swe.client.renders.debug.PipeDebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public record ClientBoundUpdatePipeCache(Map<UUID, Set<BlockPos>> data) {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundUpdatePipeCache> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, BlockPos.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new))), ClientBoundUpdatePipeCache::data, ClientBoundUpdatePipeCache::new
    );

    public static void handle(ClientBoundUpdatePipeCache message, Player player) {
        PipeDebugRenderer.requestedRefresh = true;
        PipeDebugRenderer.PIPE_RENDERS = message.data;
    }
}
