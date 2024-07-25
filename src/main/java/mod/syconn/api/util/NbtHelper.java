package mod.syconn.api.util;

import net.minecraft.core.BlockPos;
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
}
