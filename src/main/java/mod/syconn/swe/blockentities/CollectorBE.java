package mod.syconn.swe2.blockentities;

import mod.syconn.swe2.Registration;
import mod.syconn.swe2.api.blockEntity.AbstractTankBE;
import mod.syconn.swe2.util.BlockInfo;
import mod.syconn.swe2.world.container.CollectorMenu;
import mod.syconn.swe2.world.dimensions.PlanetManager;
import mod.syconn.swe2.world.dimensions.OxygenProductionManager;
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

public class CollectorBE extends AbstractTankBE implements MenuProvider, BlockInfo {

    private int ticks = 0;
    private int rate = 0;

    public CollectorBE(BlockPos pos, BlockState state) {
        super(Registration.COLLECTOR.get(), pos, state, 8000, 250);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CollectorBE e) {
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
        e.tank.handlePull(level, pos);
        e.tank.handlePush(level, pos);
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
