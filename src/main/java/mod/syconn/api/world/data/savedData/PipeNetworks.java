package mod.syconn.api.world.data.savedData;

import mod.syconn.api.world.data.PipeNetwork;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PipeNetworks extends SavedData {

    private final Map<UUID, PipeNetwork> networks = new HashMap<>();

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

    public static PipeNetworks load(CompoundTag pTag, HolderLookup.Provider lookupProvider) {
        PipeNetworks pipeNetworks = create();
        if (pTag.contains("networks")) pTag.getList("networks", Tag.TAG_COMPOUND).forEach(nbt -> {
            CompoundTag tag = (CompoundTag) nbt;
            pipeNetworks.networks.put(tag.getUUID("uuid"), PipeNetwork.deserializeNBT(tag.getCompound("network"), lookupProvider));
        });
        return pipeNetworks;
    }

    private static PipeNetworks create() {
        return new PipeNetworks();
    }

    public static PipeNetworks get(ServerLevel server) {
        return server.getDataStorage().computeIfAbsent(new Factory<>(PipeNetworks::create, PipeNetworks::load), "pipe_network");
    }
}
