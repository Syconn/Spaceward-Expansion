package mod.syconn.swe.common.blockentities;

import mod.syconn.swe.common.blocks.OxygenDisperserBlock;
import mod.syconn.swe.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AirBlockEntity extends SyncedBE {

    public int distance = 0;
    public BlockPos pos = BlockPos.ZERO;
    private boolean has_run = false;

    public AirBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.AIR.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AirBlockEntity e) {
        if (!level.isClientSide && !e.has_run) {
            for (Direction d : Direction.values()) {
                if (e.distance + 1 <= OxygenDisperserBlock.maxFill(level, e.pos))
                    OxygenDisperserBlock.addBlock(level, e.worldPosition.relative(d), e.pos, e.distance + 1);
                else level.getBlockEntity(e.pos, ModBlockEntities.DISPERSER.get()).get().failed(true);
            }
            e.has_run = true;
            e.markDirty();
        }
    }

    public void blockUpdate() {
        DisperserBE.remove(level, pos);
    }

    public void setup(int dis, BlockPos s) {
        distance = dis;
        pos = s;
        markDirty();
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("pos")) this.pos = NbtUtils.readBlockPos(pTag.getCompound("pos"));
        if (pTag.contains("distance")) this.distance = pTag.getInt("distance");
        if (pTag.contains("run")) this.has_run = pTag.getBoolean("run");
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("distance", this.distance);
        pTag.put("pos", NbtUtils.writeBlockPos(this.pos));
        pTag.putBoolean("run", this.has_run);
    }
}
