package mod.syconn.swe.blockentities;

import mod.syconn.swe.Registration;
import mod.syconn.swe.util.BlockInfo;
import mod.syconn.swe.world.container.CollectorMenu;
import mod.syconn.swe.world.dimensions.DimSettingsManager;
import mod.syconn.swe.world.dimensions.OxygenProductionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CollectorBlockEntity extends GUIFluidHandlerBlockEntity implements MenuProvider, BlockInfo {

    private int ticks = 0;
    private int rate = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.COLLECTOR.get(), pos, state, 8000);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CollectorBlockEntity e) {
        e.ticks++;
        if (e.ticks >= 20) {
            e.ticks = 0;
            double total = 0;
            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(11, 0, 11), pos.offset(-11, 11, -11))) {
                if (level.getBlockState(blockPos).is(Registration.O2_PRODUCING)) {
                    total += OxygenProductionManager.getValue(level.getBlockState(blockPos));
                }
            }
            if (DimSettingsManager.getSettings(level.dimension()).breathable()) {
                total += 186;
            }
            e.tank.fill(new FluidStack(Registration.O2_SOURCE.get(), (int) total), IFluidHandler.FluidAction.EXECUTE);
            e.rate = (int) total;
        }
        e.update();
    }

    public int getRate() {
        return rate;
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        tag.putInt("rate", rate);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        rate = pTag.getInt("rate");
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        tag.putInt("rate", rate);
        return tag;
    }

    public Component getDisplayName() {
        return Component.literal("Oxygen Collector");
    }

    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CollectorMenu(pContainerId, pPlayerInventory, worldPosition);
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
