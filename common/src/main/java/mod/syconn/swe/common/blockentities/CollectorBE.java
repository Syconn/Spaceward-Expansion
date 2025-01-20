package mod.syconn.swe.common.blockentities;

import mod.syconn.swe.blockentities.base.AbstractTankBE;
import mod.syconn.swe.common.container.CollectorMenu;
import mod.syconn.swe.server.reloaders.OxygenProductionManager;
import mod.syconn.swe.server.reloaders.PlanetManager;
import mod.syconn.swe.extra.BlockInfo;
import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.data.menu.PositionMenuData;
import mod.syconn.swe.core.BlockEntityRegister;
import mod.syconn.swe.core.ModTags;
import mod.syconn.swe.core.FluidRegister;
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

import java.util.ArrayList;
import java.util.List;

public class CollectorBE extends AbstractTankBE implements MenuProvider, BlockInfo {

    private int ticks = 0;
    private int rate = 0;

    public CollectorBE(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.COLLECTOR.get(), pos, state, 8000, 250);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CollectorBE e) {
        e.ticks++;
        if (e.ticks >= 20) {
            e.ticks = 0;
            double total = 0;
            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(11, 0, 11), pos.offset(-11, 11, -11))) {
                if (level.getBlockState(blockPos).is(ModTags.O2_PRODUCING)) {
                    total += OxygenProductionManager.getValue(level.getBlockState(blockPos));
                }
            }
            if (PlanetManager.getSettings(level.dimension()).breathable()) {
                total += 186;
            }
            e.tank.fill(new FluidHolder(FluidRegister.O2.get(), (int) total), FluidAction.EXECUTE);
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
        return new CollectorMenu(pContainerId, pPlayerInventory, new PositionMenuData(worldPosition));
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
