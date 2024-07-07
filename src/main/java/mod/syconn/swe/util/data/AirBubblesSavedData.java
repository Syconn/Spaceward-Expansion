package mod.syconn.swe.util.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import mod.syconn.swe.world.dimensions.DimSettingsManager;
import mod.syconn.swe.util.NbtHelper;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AirBubblesSavedData extends SavedData {

    private final Map<ResourceKey<Level>, Map<UUID, List<BlockPos>>> levelBlockPositions = new HashMap<>();

    public List<BlockPos> set(ResourceKey<Level> level, UUID uuid, List<BlockPos> positions) {
        Map<UUID, List<BlockPos>> oxygenMap = levelBlockPositions.computeIfAbsent(level, id -> new HashMap<>());
        setDirty();
        return oxygenMap.put(uuid, positions);
    }

    public List<BlockPos> remove(ResourceKey<Level> level, UUID uuid) {
        Map<UUID, List<BlockPos>> oxygenMap = levelBlockPositions.computeIfAbsent(level, id -> new HashMap<>());
        setDirty();
        return oxygenMap.remove(uuid);
    }

    public boolean breathable(ResourceKey<Level> level, BlockPos pos) {
        if (DimSettingsManager.getSettings(level).breathable()) return true;
        if (!levelBlockPositions.containsKey(level)) return false;
        for (List<BlockPos> positions : levelBlockPositions.get(level).values()) if (positions.contains(pos)) return true;
        return false;
    }

    public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        ListTag levelPoses = new ListTag();
        levelBlockPositions.forEach((level, positions) -> {
            CompoundTag cp = new CompoundTag();
            ListTag cpList = new ListTag();
            cp.putString("loc", level.location().toString());
            positions.forEach(((uuid, blockPos) -> {
                CompoundTag ct = new CompoundTag();
                ct.putUUID("uuid", uuid);
                ct.put("positions", NbtHelper.writePosses(blockPos));
                cpList.add(ct);
            }));
            cp.put("cdata", cpList);
            levelPoses.add(cp);
        });
        pTag.put("air_bubbles", levelPoses);
        return pTag;
    }

    public void read(CompoundTag tag) {
        levelBlockPositions.clear();
        if(tag.contains("air_bubbles", Tag.TAG_LIST)) {
            tag.getList("air_bubbles", Tag.TAG_COMPOUND).forEach(nbt -> {
                CompoundTag outerData = (CompoundTag) nbt;
                Map<UUID, List<BlockPos>> oxygenMap = new HashMap<>();
                outerData.getList("cdata", Tag.TAG_COMPOUND).forEach(nNBT -> {
                    CompoundTag ct = (CompoundTag) nNBT;
                    oxygenMap.put(ct.getUUID("uuid"), NbtHelper.readPosses(ct.getCompound("positions")));
                });
                levelBlockPositions.put(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(outerData.getString("loc"))), oxygenMap);
            });
        }
    }

    public static AirBubblesSavedData create() {
        return new AirBubblesSavedData();
    }

    public static AirBubblesSavedData load(CompoundTag tag) {
        AirBubblesSavedData data = create();
        data.read(tag);
        return data;
    }

    public static AirBubblesSavedData get() { // TODO FIX
        return ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage().computeIfAbsent(AirBubblesSavedData::load, AirBubblesSavedData::create, "air_bubbles");
    }
}
