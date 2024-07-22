package mod.syconn.swe.blockentities;

import mod.syconn.swe.Registration;
import mod.syconn.swe.blocks.OxygenDisperser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AirBlockEntity extends BlockEntity {

    public int distance = 0;
    public BlockPos pos = BlockPos.ZERO;
    private boolean has_run = false;

    public AirBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(Registration.AIR.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AirBlockEntity e) {
        if (!level.isClientSide && !e.has_run) {
            for (Direction d : Direction.values()) {
                if (e.distance + 1 <= OxygenDisperser.maxFill(level, e.pos)) {
                    OxygenDisperser.addBlock(level, e.worldPosition.relative(d), e.pos, e.distance + 1);
                } else {
                    level.getBlockEntity(e.pos, Registration.DISPERSER.get()).get().failed(true);
                }
            }
            e.has_run = true;
            e.update();
        }
    }

    public void blockUpdate() {
        DisperserBE.remove(level, pos);
    }

    public void setup(int dis, BlockPos s) {
        distance = dis;
        pos = s;
        update();
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        pos = NbtUtils.readBlockPos(pTag, "pos").orElse(null);
        if (pTag.contains("distance")) distance = pTag.getInt("distance");
        if (pTag.contains("run")) has_run = pTag.getBoolean("run");
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("distance", distance);
        pTag.put("pos", NbtUtils.writeBlockPos(pos));
        pTag.putBoolean("run", has_run);
    }

    protected void update(){
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
    }
}
