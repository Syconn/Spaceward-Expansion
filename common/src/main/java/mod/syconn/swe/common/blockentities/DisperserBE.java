package mod.syconn.swe.common.blockentities;

import mod.syconn.swe.common.blocks.DispersedAirBlock;
import mod.syconn.swe.common.blocks.OxygenDisperserBlock;
import mod.syconn.swe.core.ModBlockEntities;
import mod.syconn.swe.core.ModBlocks;
import mod.syconn.swe.server.savedData.AirBubblesSavedData;
import mod.syconn.swe.util.TagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DisperserBE extends AbstractTankBE {

    public List<BlockPos> list = new ArrayList<>();
    public int maxFill = 20; // TODO CONFIG THIS SHIT ALSO REDO
    private int testRate = 0;
    private int lowerRate = 0;
    private final int rate = 15;
    private int o2Usage;
    private UUID uuid;
    private boolean active = false;
    private boolean enabled = true;

    public DisperserBE(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.DISPERSER.get(), p_155229_, p_155230_, 15, 1000);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DisperserBE e) {
        if (e.enabled) {
            if (e.tank.getFluidStack().getAmount() > 0) {
                e.testRate--;
                if (e.testRate <= 0) {
                    e.testRate = 100;
                    OxygenDisperserBlock.addBlock(level, pos.relative(Direction.UP), pos, 1);
                    level.scheduleTick(pos, ModBlocks.OXYGEN_DISPERSER.get(), 25, TickPriority.NORMAL);
                }
            } else AirBubblesSavedData.get((ServerLevel) level).remove(level.dimension(), e.uuid);

            if (e.active) {
                if (e.list.size() / e.rate > e.tank.getCapacity()) {
                    e.active = false;
                    e.list.clear();
                    AirBubblesSavedData.get((ServerLevel) level).remove(level.dimension(), e.uuid);
                } else {
                    if (e.lowerRate <= 0) {
                        e.lowerRate = 10;
                        e.tank.pull(e.list.size() / e.rate, true);
                        e.o2Usage = e.list.size() / e.rate;
                    } else e.lowerRate--;
                }
            } else e.o2Usage = 0;
        } else e.o2Usage = 0;
        e.markDirty();
    }

    public static void remove(Level level, BlockPos defPos) {
        if (level.getBlockEntity(defPos, ModBlockEntities.DISPERSER.get()).isPresent()) {
            List<BlockPos> list = level.getBlockEntity(defPos, ModBlockEntities.DISPERSER.get()).get().list;
            for (BlockPos pos : list) if (level.getBlockState(pos).getBlock() instanceof DispersedAirBlock) level.removeBlock(pos, false);
        }
    }

    public void failed(boolean t) {
        if (level instanceof ServerLevel) {
            for (BlockPos pos : list) if (level.getBlockState(pos).getBlock() instanceof DispersedAirBlock) level.removeBlock(pos, false);
            if (t) {
                active = false;
                list.clear();
                AirBubblesSavedData.get((ServerLevel) level).remove(level.dimension(), uuid);
            } else {
                if (list.size() / rate > tank.getFluidStack().getAmount()) {
                    active = false;
                    list.clear();
                    AirBubblesSavedData.get((ServerLevel) level).remove(level.dimension(), uuid);
                } else {
                    active = true;
                    tank.pull(list.size() / rate, true);
                    AirBubblesSavedData.get((ServerLevel) level).set(level.dimension(), uuid, list);
                }
            }
            markDirty();
        }
    }

    public void toggleEnabled() {
        if (level != null) {
            this.enabled = !this.enabled;
            if (this.enabled && tank.getFluidStack().getAmount() > 0) {
                testRate = 100;
                OxygenDisperserBlock.addBlock(level, worldPosition.relative(Direction.UP), worldPosition, 1);
                level.scheduleTick(worldPosition, ModBlocks.OXYGEN_DISPERSER.get(), 25, TickPriority.NORMAL);
            }
            markDirty();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID randomUUID) {
        if(this.uuid == null) {
            this.uuid = randomUUID;
        }
        markDirty();
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("list", TagUtil.writeBlockPositions(list));
        pTag.putInt("fill", maxFill);
        pTag.putBoolean("active", active);
        pTag.putBoolean("enabled", enabled);
        pTag.putInt("usage", o2Usage);
        if (this.uuid != null) pTag.putUUID("DisperserUUID", this.uuid);
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        list = TagUtil.readBlockPositions(pTag.getCompound("list"));
        maxFill = pTag.getInt("fill");
        active = pTag.getBoolean("active");
        enabled = pTag.getBoolean("enabled");
        o2Usage = pTag.getInt("usage");
        if(pTag.hasUUID("DisperserUUID")) this.uuid = pTag.getUUID("DisperserUUID");
    }
}
