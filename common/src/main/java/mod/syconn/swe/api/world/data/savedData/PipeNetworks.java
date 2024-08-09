package mod.syconn.swe.api.world.data.savedData;

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
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PipeNetworks extends SavedData {

    private final Map<UUID, PipeNetwork> networks = new HashMap<>();
    private int tick = 0;
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
                createLine(uuid, conjoin(pos, connectionUUIDS));
            }
            else createLine(uuid, conjoin(pos, connectionUUIDS));
            setDirty();
            return uuid;
        }
        return null;
    }

    public void removePipe(UUID uuid, BlockPos pos) {
        if (networks.containsKey(uuid)) {
            if (networks.get(uuid).removePipe(pos)) networks.remove(uuid);
            else validate(uuid);
        }
        setDirty();
    }

    public void updatePipe(UUID uuid, BlockPos pos) {
        if (networks.containsKey(uuid)) networks.get(uuid).updatePipe(level, pos);
        setDirty();
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

    private void validate(UUID networkID) {
        List<BlockPos> unchecked = networks.get(networkID).getPipes();
        networks.remove(networkID);
        while (!unchecked.isEmpty()) {
            createLine(networkID, findValidLine(unchecked.getFirst(), new ArrayList<>(), unchecked));
            networkID = UUID.randomUUID();
        }
    }

    private List<BlockPos> findValidLine(BlockPos checkPos, List<BlockPos> checked, List<BlockPos> unchecked) {
        checked.add(checkPos);
        unchecked.remove(checkPos);
        for (Direction direction : Direction.values()) if (unchecked.contains(checkPos.relative(direction))) findValidLine(checkPos.relative(direction), checked, unchecked);
        return checked;
    }

    private void createLine(UUID networkID, List<BlockPos> positions) {
        if (networks.containsKey(networkID)) positions.addAll(networks.get(networkID).getPipes());
        networks.put(networkID, new PipeNetwork(networkID, level, positions));
        positions.forEach(pos -> { if (level.getBlockEntity(pos) instanceof AbstractPipeBE pipeBE) pipeBE.setNetworkID(networkID); });
    }

    private void tick() {
        tick++;
        if (tick > 2) {
            fixList();
            networks.forEach((uuid, network) -> network.tick(level));
            tick = 0;
            setDirty();
        }
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

    private void renderPipes(Level level) {
        if (render() && level instanceof ServerLevel sl) sl.getPlayers(LivingEntity::isAlive).forEach(serverPlayer -> Channel.sendToPlayer(new ClientBoundUpdatePipeCache(getDataMap()), serverPlayer));
    }

    protected boolean render() {
        return true;
    }

    public void setDirty() {
        renderPipes(level);
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
        pTag.putInt("tick", tick);
        return pTag;
    }

    public static PipeNetworks load(ServerLevel serverLevel, CompoundTag pTag, HolderLookup.Provider lookupProvider) {
        PipeNetworks pipeNetworks = create(serverLevel);
        if (pTag.contains("networks")) pTag.getList("networks", Tag.TAG_COMPOUND).forEach(nbt -> {
            CompoundTag tag = (CompoundTag) nbt;
            pipeNetworks.networks.put(tag.getUUID("uuid"), PipeNetwork.deserializeNBT(tag.getCompound("network")));
            pipeNetworks.tick = pTag.getInt("tick");
        });
        return pipeNetworks;
    }

    private static PipeNetworks create(ServerLevel serverLevel) {
        return new PipeNetworks(serverLevel);
    }

    public static PipeNetworks get(ServerLevel server) {
        return server.getDataStorage().computeIfAbsent(new Factory<>(() -> create(server), (t, p) -> load(server, t, p)), "pipe_network");
    }

    public static void onTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel sl) get(sl).tick();
    }
}
