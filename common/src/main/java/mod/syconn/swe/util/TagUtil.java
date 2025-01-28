package mod.syconn.swe.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagUtil {

    public static CompoundTag writeBlockPositions(List<BlockPos> positions){
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        positions.forEach(pos -> {
            CompoundTag nbt = new CompoundTag();
            nbt.put("pos", NbtUtils.writeBlockPos(pos));
            list.add(nbt);
        });
        tag.put("positions", list);
        return tag;
    }

    public static List<BlockPos> readBlockPositions(CompoundTag tag){
        List<BlockPos> positions = new ArrayList<>();
        if (tag.contains("positions")) tag.getList("positions", Tag.TAG_COMPOUND).forEach(nbt -> positions.add(NbtUtils.readBlockPos(((CompoundTag) nbt).getCompound("pos"))));
        return positions;
    }

    public static CompoundTag writeDirections(List<Direction> directions){
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        directions.forEach(direction -> {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("direction", direction.get3DDataValue());
            list.add(nbt);
        });
        tag.put("directions", list);
        return tag;
    }

    public static List<Direction> readDirections(CompoundTag tag){
        List<Direction> directions = new ArrayList<>();
        if (tag.contains("directions")) tag.getList("directions", Tag.TAG_COMPOUND).forEach(nbt -> directions.add(Direction.from3DDataValue(((CompoundTag) nbt).getInt("direction"))));
        return directions;
    }
}
