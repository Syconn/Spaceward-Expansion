package mod.syconn.swe2.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class NbtHelper {

    public static CompoundTag writePositionList(List<BlockPos> positions){
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

    public static List<BlockPos> readPositionList(CompoundTag tag){
        List<BlockPos> positions = new ArrayList<>();
        if (tag.contains("positions")) tag.getList("positions", Tag.TAG_COMPOUND).forEach(nbt -> positions.add(NbtUtils.readBlockPos((CompoundTag) nbt, "pos").get()));
        return positions;
    }

    public static CompoundTag writeDirectionList(List<Direction> directions){
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

    public static List<Direction> readDirectionList(CompoundTag tag){
        List<Direction> directions = new ArrayList<>();
        if (tag.contains("directions")) tag.getList("directions", Tag.TAG_COMPOUND).forEach(nbt -> directions.add(Direction.from3DDataValue(((CompoundTag) nbt).getInt("direction"))));
        return directions;
    }
}
