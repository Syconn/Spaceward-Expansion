package mod.syconn.api.world.data.savedData;

import com.google.common.collect.Sets;
import mod.syconn.api.blockEntity.AbstractPipeBE;
import mod.syconn.api.client.packets.ClientBoundUpdatePipeCache;
import mod.syconn.api.util.ListTools;
import mod.syconn.api.world.data.PipeNetwork;
import mod.syconn.swe.network.Channel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PipeNetworks extends SavedData {

    private final Map<UUID, PipeNetwork> networks = new HashMap<>();
    private final ServerLevel level;

    public PipeNetworks(ServerLevel level) {
        this.level = level;
    }

    public @Nullable UUID addPipe(BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AbstractPipeBE pipeBE) {
            List<UUID> connectionUUIDS = new ArrayList<>();
            for (Direction direction : Direction.values())
                if (pipeBE.canConnectToPipe(level, pos, direction) && level.getBlockEntity(pos.relative(direction)) instanceof AbstractPipeBE otherPipe && !connectionUUIDS.contains(otherPipe.getNetworkID()))
                    connectionUUIDS.add(otherPipe.getNetworkID());
            UUID uuid = ListTools.getListMostCommonElement(connectionUUIDS, UUID.randomUUID());
            if (connectionUUIDS.isEmpty()) networks.put(uuid, new PipeNetwork(uuid, level, pos));
            else if (connectionUUIDS.contains(uuid) && networks.containsKey(uuid)) {
                connectionUUIDS.remove(uuid);
                networks.get(uuid).addAllPipes(level, conjoin(pos, connectionUUIDS));
            }
            else networks.put(uuid, new PipeNetwork(uuid, level, conjoin(pos, connectionUUIDS)));
            setDirty();
            return uuid;
        }
        return null;
    }

    public void removePipe(BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AbstractPipeBE pipeBE && networks.containsKey(pipeBE.getNetworkID())) {
            if (networks.get(pipeBE.getNetworkID()).removePipe(pos)) networks.remove(pipeBE.getNetworkID());
            else validLine(pipeBE.getNetworkID());
        }
        setDirty();
    }

    public void fixList() {
        List<UUID> removeElement = new ArrayList<>();
        for (Map.Entry<UUID, PipeNetwork> entry : networks.entrySet()) {
            if (entry.getValue().getPipes().isEmpty()) removeElement.add(entry.getKey());
            else {
                entry.getValue().getPipes().forEach(pos -> {
                    if (!(level.getBlockEntity(pos) instanceof AbstractPipeBE)) removeElement.add(entry.getKey());
                });
            }
        }
        removeElement.forEach(networks::remove);
        setDirty();
    }

    private void validLine(UUID uuid) { // TODO BROKEN
        if (networks.containsKey(uuid)) {
            List<BlockPos> validPosList = new ArrayList<>();
            List<BlockPos> posList = networks.get(uuid).getPipes();
            validPosList.add(posList.get(0));
            for (Direction d : Direction.values()) if (posList.contains(posList.get(0).relative(d))) validPosList.add(posList.get(0).relative(d));
            int lastSize = 0;
            while (lastSize < validPosList.size()) {
                lastSize = validPosList.size();
                List<BlockPos> testPos = List.of(validPosList.toArray(BlockPos[]::new));
                for (BlockPos pos : testPos)
                    for (Direction d : Direction.values())
                        if (posList.contains(pos.relative(d)) && !validPosList.contains(pos.relative(d)))
                            validPosList.add(pos.relative(d));
            }
            newLine(uuid, validPosList);
            for (BlockPos pos : posList) {
                if (!validPosList.contains(pos)) {
                    validLine(uuid);
                    return;
                }
            }
        }
    }

    private void newLine(UUID oldUUID, List<BlockPos> positions) {
        for (BlockPos pos : positions) if (networks.containsKey(oldUUID) && networks.get(oldUUID).removePipe(pos)) networks.remove(oldUUID);
        UUID uuid = UUID.randomUUID();
        networks.put(uuid, new PipeNetwork(uuid, level, positions));
        for (BlockPos pos : positions) if (level.getBlockEntity(pos) instanceof AbstractPipeBE be) be.setNetworkID(uuid);
    }

    private void debugPipes(Level level) {
        if (render() && level instanceof ServerLevel sl) sl.getPlayers(LivingEntity::isAlive).forEach(serverPlayer -> Channel.sendToPlayer(new ClientBoundUpdatePipeCache(getDataMap()), serverPlayer));
    }

    private List<BlockPos> conjoin(BlockPos pos, List<UUID> connectionUUIDS) {
        List<BlockPos> positions = new ArrayList<>();
        positions.add(pos);
        connectionUUIDS.forEach(id -> positions.addAll(endNetwork(id)));
        return positions;
    }

    private List<BlockPos> endNetwork(UUID uuid) {
        List<BlockPos> positions = new ArrayList<>(networks.get(uuid).getPipes());
        networks.remove(uuid);
        return positions;
    }

    protected boolean render() {
        return true;
    }

    public void setDirty() {
        debugPipes(level);
        super.setDirty();
    }

    public Map<UUID, Set<BlockPos>> getDataMap() {
        Map<UUID, Set<BlockPos>> dataMap = new HashMap<>();
        networks.forEach(((uuid, network) -> dataMap.put(uuid, Sets.newHashSet(network.getPipes()))));
        return dataMap;
    }

    public @NotNull CompoundTag save(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, PipeNetwork> entry : networks.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("uuid", entry.getKey());
            tag.put("network", entry.getValue().serializeNBT());
            list.add(tag);
        }
        pTag.put("networks", list);
        return pTag;
    }

    public static PipeNetworks load(ServerLevel serverLevel, CompoundTag pTag, HolderLookup.Provider lookupProvider) {
        PipeNetworks pipeNetworks = create(serverLevel);
        if (pTag.contains("networks")) pTag.getList("networks", Tag.TAG_COMPOUND).forEach(nbt -> {
            CompoundTag tag = (CompoundTag) nbt;
            pipeNetworks.networks.put(tag.getUUID("uuid"), PipeNetwork.deserializeNBT(tag.getCompound("network"), lookupProvider));
        });
        return pipeNetworks;
    }

    private static PipeNetworks create(ServerLevel serverLevel) {
        return new PipeNetworks(serverLevel);
    }

    public static PipeNetworks get(ServerLevel server) {
        return server.getDataStorage().computeIfAbsent(new Factory<>(() -> create(server), (t, p) -> load(server, t, p)), "pipe_network");
    }
}
