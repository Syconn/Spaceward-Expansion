package mod.syconn.swe.blockentities;

import mod.syconn.swe.Registration;
import mod.syconn.api.blockEntity.AbstractTankBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import mod.syconn.swe.blocks.DispersibleAirBlock;
import mod.syconn.swe.world.container.DisperserMenu;
import mod.syconn.swe.util.BlockInfo;
import mod.syconn.swe.util.NbtHelper;
import mod.syconn.swe.world.data.savedData.AirBubblesSavedData;
import net.minecraft.world.ticks.TickPriority;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static mod.syconn.swe.blocks.OxygenDisperser.addBlock;

public class DisperserBlockEntity extends AbstractTankBE implements MenuProvider, BlockInfo {

    //TODO change to blocks and look for blockUpdates
    //  - Pipe dont show fluid

    public List<BlockPos> list = new ArrayList<>();
    public int maxFill = 20; // TODO CONFIG THIS SHIT ALSO REDO
    private int testRate = 0;
    private int lowerRate = 0;
    private final int rate = 15;
    private int o2Usage;
    private UUID uuid;
    private boolean active = false;
    private boolean enabled = true;

    public DisperserBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(Registration.DISPERSER.get(), p_155229_, p_155230_, 1000);
        this.tank = new FluidTank(1000){
            public void onContentsChanged() { markDirty(); }
            public boolean isFluidValid(FluidStack stack) { return validator.test(stack) && stack.getFluid() == Registration.O2.get(); }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DisperserBlockEntity e) {
        if (e.enabled) {
            if (e.tank.getFluidInTank(0).getAmount() > 0) {
                e.testRate--;
                if (e.testRate <= 0) {
                    e.testRate = 100;
                    addBlock(level, pos.relative(Direction.UP), pos, 1);
                    level.scheduleTick(pos, Registration.OXYGEN_DISPERSER.get(), 25, TickPriority.NORMAL);
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
        } else e.o2Usage = 0;
        e.markDirty();
    }

    public static void remove(Level level, BlockPos defPos) {
        if (level.getBlockEntity(defPos, Registration.DISPERSER.get()).isPresent()) {
            List<BlockPos> list = level.getBlockEntity(defPos, Registration.DISPERSER.get()).get().list;
            for (BlockPos pos : list) if (level.getBlockState(pos).getBlock() instanceof DispersibleAirBlock) level.removeBlock(pos, false);
        }
    }

    public void failed(boolean t) {
        for (BlockPos pos : list) if (level.getBlockState(pos).getBlock() instanceof DispersibleAirBlock) level.removeBlock(pos, false);
        if (t) {
            active = false;
            list.clear();
            AirBubblesSavedData.get().remove(level.dimension(), uuid);
        } else {
            if (list.size() / rate > tank.getFluidAmount()) {
                active = false;
                list.clear();
                AirBubblesSavedData.get().remove(level.dimension(), uuid);
            } else {
                active = true;
                tank.drain(list.size() / rate, IFluidHandler.FluidAction.EXECUTE);
                AirBubblesSavedData.get().set(level.dimension(), uuid, list);
            }
        }
        markDirty();
    }

    public void toggleEnabled() {
        this.enabled = !this.enabled;
        if (this.enabled && tank.getFluidInTank(0).getAmount() > 0) {
            testRate = 100;
            addBlock(level, worldPosition.relative(Direction.UP), worldPosition, 1);
            level.scheduleTick(worldPosition, Registration.OXYGEN_DISPERSER.get(), 25, TickPriority.NORMAL);
        }
        markDirty();
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

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("list", NbtHelper.writePosses(list));
        pTag.putInt("fill", maxFill);
        pTag.putBoolean("active", active);
        pTag.putBoolean("enabled", enabled);
        pTag.putInt("usage", o2Usage);
        if (this.uuid != null) pTag.putUUID("DisperserUUID", this.uuid);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        list = NbtHelper.readPosses(pTag.getCompound("list"));
        maxFill = pTag.getInt("fill");
        active = pTag.getBoolean("active");
        enabled = pTag.getBoolean("enabled");
        o2Usage = pTag.getInt("usage");
        if(pTag.hasUUID("DisperserUUID")) this.uuid = pTag.getUUID("DisperserUUID");
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
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

    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new DisperserMenu(pContainerId, pPlayerInventory, worldPosition);
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
