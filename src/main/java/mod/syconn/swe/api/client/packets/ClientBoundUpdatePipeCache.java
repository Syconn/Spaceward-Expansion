package mod.syconn.swe2.api.client.packets;

import io.netty.buffer.ByteBuf;
import mod.syconn.swe2.api.Constants;
import mod.syconn.swe2.api.client.debug.PipeNetworkRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.*;

public record ClientBoundUpdatePipeCache(Map<UUID, Set<BlockPos>> data) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientBoundUpdatePipeCache> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.ID, "update_pipe_cache"));
    public static final StreamCodec<ByteBuf, ClientBoundUpdatePipeCache> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, BlockPos.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new))), ClientBoundUpdatePipeCache::data, ClientBoundUpdatePipeCache::new
    );
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ClientBoundUpdatePipeCache message, IPayloadContext context) {
        context.enqueueWork(() -> {
            PipeNetworkRenderer.requestedRefresh = true;
            PipeNetworkRenderer.PIPE_RENDERS = message.data;
        });
    }
}
