package mod.syconn.swe.blockentities;

import net.minecraft.core.BlockPos;
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
import mod.syconn.swe.world.container.CollectorMenu;
import mod.syconn.swe.world.data.types.DimSettingsManager;
import mod.syconn.swe.world.data.types.OxygenProductionManager;
import mod.syconn.swe.util.BlockInfo;

import java.util.ArrayList;
import java.util.List;

public class CollectorBlockEntity extends GUIFluidHandlerBlockEntity implements MenuProvider, BlockInfo {

    private int ticks = 0;
    private int rate = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.COLLECTOR.get(), pos, state, 8000, topOrBottom());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CollectorBlockEntity e) {
        e.ticks++;
        if (e.ticks >= 20) {
            e.ticks = 0;
            double total = 0;
            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(11, 0, 11), pos.offset(-11, 11, -11))) {
                if (level.getBlockState(blockPos).is(ModTags.O2_PRODUCING)) {
                    total += OxygenProductionManager.getValue(level.getBlockState(blockPos));
                }
            }
            if (DimSettingsManager.getSettings(level.dimension()).breathable()) {
                total += 186;
            }
            e.tank.fill(new FluidStack(ModFluids.SOURCE_O2_FLUID.get(), (int) total), IFluidHandler.FluidAction.EXECUTE);
            e.rate = (int) total;
        }
        e.update();
    }

    public int getRate() {
        return rate;
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("rate", rate);
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        rate = tag.getInt("rate");
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("rate", rate);
        return tag;
    }

    public Component getDisplayName() {
        return Component.literal("Oxygen Collector");
    }

    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new CollectorMenu(p_39954_, p_39955_, this);
    }

    public int getFluidRate() {
        return rate;
    }

    public int getPowerRate() {
        return 0;
    }

    public List<Component> getExtraInfo() {
        return new ArrayList<>();
    }
}
