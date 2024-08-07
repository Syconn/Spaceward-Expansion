package mod.syconn.swe.blockentities;

import mod.syconn.swe.Registration;
import mod.syconn.api.blockEntity.AbstractTankBE;
import mod.syconn.swe.util.BlockInfo;
import mod.syconn.swe.world.container.CollectorMenu;
import mod.syconn.swe.world.dimensions.PlanetManager;
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

import java.util.ArrayList;
import java.util.List;

public class CollectorBlockEntity extends AbstractTankBE implements MenuProvider, BlockInfo {

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
            if (PlanetManager.getSettings(level.dimension()).breathable()) {
                total += 186;
            }
            e.tank.fill(new FluidStack(Registration.O2.get(), (int) total), IFluidHandler.FluidAction.EXECUTE);
            e.rate = (int) total;
        }
        e.markDirty();
    }

    public int getRate() {
        return rate;
    }

    protected void saveClientData(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveClientData(pTag, pRegistries);
        pTag.putInt("rate", rate);
    }

    protected void loadClientData(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadClientData(pTag, pRegistries);
        rate = pTag.getInt("rate");
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
