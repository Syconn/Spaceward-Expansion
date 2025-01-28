package mod.syconn.swe.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swe.client.renders.debug.PipeDebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class MessageUpdateClientPipeCache {

    private final Map<UUID, Set<BlockPos>> data;

    public MessageUpdateClientPipeCache(Map<UUID, Set<BlockPos>> data) {
        this.data = data;
    }

    public MessageUpdateClientPipeCache(FriendlyByteBuf buf) {
        this(buf.readMap(FriendlyByteBuf::readUUID, byteBuf -> new HashSet<>(byteBuf.readList(FriendlyByteBuf::readBlockPos))));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeMap(this.data, FriendlyByteBuf::writeUUID, ((byteBuf, blockPos) -> byteBuf.writeCollection(blockPos, FriendlyByteBuf::writeBlockPos)));
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            PipeDebugRenderer.requestedRefresh = true;
            PipeDebugRenderer.PIPE_RENDERS = data;
        });
    }
}
