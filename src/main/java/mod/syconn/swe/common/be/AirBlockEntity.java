package mod.syconn.swe.common.be;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import mod.syconn.swe.block.OxygenDisperser;
import mod.syconn.swe.init.ModBlockEntity;

public class AirBlockEntity extends BlockEntity {

    public int distance = 0;
    public BlockPos pos = BlockPos.ZERO;
    private boolean has_run = false;

    public AirBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntity.AIR.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AirBlockEntity e) {
        if (!level.isClientSide && !e.has_run) {
            for (Direction d : Direction.values()) {
                if (e.distance + 1 <= OxygenDisperser.maxFill(level, e.pos)) {
                    OxygenDisperser.addBlock(level, e.worldPosition.relative(d), e.pos, e.distance + 1);
                } else {
                    level.getBlockEntity(e.pos, ModBlockEntity.DISPERSER.get()).get().failed(true);
                }
            }
            e.has_run = true;
        }
    }

    public void blockUpdate() {
        DisperserBlockEntity.remove(level, pos);
    }

    public void setup(int dis, BlockPos s) {
        distance = dis;
        pos = s;
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("distance")) distance = tag.getInt("distance");
        if (tag.contains("pos")) pos = NbtUtils.readBlockPos(tag.getCompound("pos"));
        if (tag.contains("run")) has_run = tag.getBoolean("run");
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("distance", distance);
        tag.put("pos", NbtUtils.writeBlockPos(pos));
        tag.putBoolean("run", has_run);
    }
}
