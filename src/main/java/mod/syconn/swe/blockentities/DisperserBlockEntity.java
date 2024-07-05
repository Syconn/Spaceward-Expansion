package mod.syconn.swe.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import mod.syconn.swe.blocks.DispersibleAirBlock;
import mod.syconn.swe.world.container.DisperserMenu;
import mod.syconn.swe.util.BlockInfo;
import mod.syconn.swe.util.NbtHelper;
import mod.syconn.swe.util.data.AirBubblesSavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DisperserBlockEntity extends GUIFluidHandlerBlockEntity implements MenuProvider, BlockInfo {

    //TODO change to blocks and look for blockUpdates
    //  - Pipe dont show fluid

    public List<BlockPos> list = new ArrayList<>();
    public int maxFill = 20;
    private int testRate = 0;
    private int lowerRate = 0;
    private final int rate = 15;
    private int o2Usage;
    private UUID uuid;
    private boolean active = false;
    private boolean enabled = true;

    public DisperserBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntity.DISPERSER.get(), p_155229_, p_155230_, 1000, List.of(Direction.DOWN));
        this.tank = new FluidTank(1000){
            private void onContentsChanged() { update(); }

            public int fill(FluidStack resource, FluidAction action) {
                if (fluid.isEmpty()) updateTextures(resource);
                return super.fill(resource, action);
            }

            public boolean isFluidValid(FluidStack stack) { return validator.test(stack) && stack.getFluid() == ModFluids.SOURCE_O2_FLUID.get(); }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DisperserBlockEntity e) {
        if (e.enabled) {
            if (e.tank.getFluidInTank(0).getAmount() > 0) {
                e.testRate--;
                if (e.testRate <= 0) {
                    e.testRate = 100;
//                    addBlock(level, pos.relative(Direction.UP), pos, 1);
//                    level.scheduleTick(pos, ModInit.OXYGEN_DISPERSER.get(), 25, TickPriority.NORMAL);
                }
            } else { // TODO OPTIMISE
                AirBubblesSavedData.get().remove(level.dimension(), e.uuid);
            }

            if (e.active) {
                if (e.list.size() / e.rate > e.tank.getFluidAmount()) {
                    e.active = false;
                    e.list.clear();
                    AirBubblesSavedData.get().remove(level.dimension(), e.uuid);
                } else {
                    if (e.lowerRate <= 0) {
                        e.lowerRate = 10;
                        e.tank.drain(e.list.size() / e.rate, IFluidHandler.FluidAction.EXECUTE);
                        e.o2Usage = e.list.size() / e.rate;
                    } else {
                        e.lowerRate--;
                    }
                }
            } else e.o2Usage = 0;
            e.update();
        } else e.o2Usage = 0;
    }

    public static void remove(Level level, BlockPos defPos) {
        List<BlockPos> list = level.getBlockEntity(defPos, ModBlockEntity.DISPERSER.get()).get().list;
        for (BlockPos pos : list) if (level.getBlockState(pos).getBlock() instanceof DispersibleAirBlock) level.removeBlock(pos, false);
    }

    public void failed(boolean t) {
//        for (BlockPos pos : list) if (level.getBlockState(pos).getBlock() instanceof DispersibleAirBlock) level.removeBlock(pos, false);
//        if (t) {
//            active = false;
//            list.clear();
//            AirBubblesSavedData.get().remove(level.dimension(), uuid);
//        } else {
//            if (list.size() / rate > tank.getFluidAmount()) {
//                active = false;
//                list.clear();
//                AirBubblesSavedData.get().remove(level.dimension(), uuid);
//            } else {
//                active = true;
//                tank.drain(list.size() / rate, IFluidHandler.FluidAction.EXECUTE);
//                AirBubblesSavedData.get().set(level.dimension(), uuid, list);
//            }
//        }
    }

    public void toggleEnabled() {
        this.enabled = !this.enabled;
        if (this.enabled && tank.getFluidInTank(0).getAmount() > 0) {
            testRate = 100;
//            addBlock(level, worldPosition.relative(Direction.UP), worldPosition, 1);
//            level.scheduleTick(worldPosition, ModInit.OXYGEN_DISPERSER.get(), 25, TickPriority.NORMAL);
        }
        update();
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
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("list", NbtHelper.writePosses(list));
        tag.putInt("fill", maxFill);
        tag.putBoolean("active", active);
        tag.putBoolean("enabled", enabled);
        tag.putInt("usage", o2Usage);
        if (this.uuid != null) tag.putUUID("DisperserUUID", this.uuid);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        list = NbtHelper.readPosses(tag.getCompound("list"));
        maxFill = tag.getInt("fill");
        active = tag.getBoolean("active");
        enabled = tag.getBoolean("enabled");
        o2Usage = tag.getInt("usage");
        if(tag.hasUUID("DisperserUUID")) this.uuid = tag.getUUID("DisperserUUID");
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("list", NbtHelper.writePosses(list));
        tag.putInt("fill", maxFill);
        tag.putBoolean("active", active);
        tag.putBoolean("enabled", enabled);
        tag.putInt("usage", o2Usage);
        if (this.uuid != null) tag.putUUID("DisperserUUID", this.uuid);
        return tag;
    }

    public Component getDisplayName() {
        return Component.literal("Oxygen Disperser");
    }

    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new DisperserMenu(p_39954_, p_39955_, this);
    }

    public int getFluidRate() {
        return o2Usage;
    }

    public int getPowerRate() {
        return 0;
    }

    public List<Component> getExtraInfo() {
        return List.of(Component.literal(""), Component.literal("Actively"));
    }
}
