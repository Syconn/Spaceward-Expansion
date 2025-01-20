package mod.syconn.swe.common.blockentities;

import mod.syconn.swe.blockentities.base.AbstractTankBE;
import mod.syconn.swe.server.container.TankMenu;
import mod.syconn.swe.extra.data.menu.PositionMenuData;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TankBE extends AbstractTankBE implements MenuProvider, Container {

    private final int fillSpeed = 500;
    private final SimpleContainer container = new SimpleContainer(3);

    public TankBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TANK.get(), pos, state, 16000, 500);
    }

    protected void saveClientData(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveClientData(pTag, provider);
        ContainerHelper.saveAllItems(pTag, container.getItems(), provider);
    }

    protected void loadClientData(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadClientData(pTag, provider);
        ContainerHelper.loadAllItems(pTag, container.getItems(), provider);
    }

    public Component getDisplayName() {
        return Component.literal("Fluid Tank Screen");
    }

    public AbstractContainerMenu createMenu(int id, Inventory p_39955_, Player p_39956_) {
        return new TankMenu(id, p_39955_, new PositionMenuData(worldPosition));
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TankBE e) {
        if (!level.isClientSide) {
            ItemStack itemStack = e.container.getItem(0);
            if (Services.FLUID_HANDLER.has(itemStack)) Services.FLUID_HELPER.handleInventoryMaxTransfer(e.tank, itemStack, e.container, 0, 1, e);

            itemStack = e.getItem(2);
            if (Services.FLUID_HANDLER.has(itemStack)) Services.FLUID_HELPER.fillItemStackFromBlock(e, e.tank, itemStack, e.fillSpeed, 2);

            e.tank.handlePush(level, pos);
            e.tank.handlePull(level, pos);
            e.markDirty();
        }
    }

    public int getContainerSize() {
        return container.getContainerSize();
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }

    public ItemStack getItem(int i) {
        return container.getItem(i);
    }

    public ItemStack removeItem(int i, int i2) {
        return container.removeItem(i, i2);
    }

    public ItemStack removeItemNoUpdate(int i) {
        return container.removeItemNoUpdate(i);
    }

    public void setItem(int i, ItemStack itemStack) {
        container.setItem(i, itemStack);
    }

    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public void clearContent() {
        container.clearContent();
    }
}
