package mod.syconn.swe.blockentities;

import mod.syconn.api.blockEntity.AbstractTankBE;
import mod.syconn.swe.Registration;
import mod.syconn.api.blockEntity.AbstractTankBE;
import mod.syconn.swe.util.FluidHelper;
import mod.syconn.swe.world.container.TankMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TankBE extends AbstractTankBE implements MenuProvider {

    private final int fillSpeed = 500;
    private final ItemStackHandler items = new ItemStackHandler(getContainerSize()) {
        public void onContentsChanged(int slot) { markDirty(); }
    };
    private final Lazy<IItemHandler> holder = Lazy.of(() -> items);

    public TankBE(BlockPos pos, BlockState state) {
        super(Registration.TANK.get(), pos, state, 16000, 500);
    }

    public ItemStackHandler getItems() {
        return items;
    }

    protected void saveClientData(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveClientData(pTag, pRegistries);
        pTag.put("Inventory", items.serializeNBT(pRegistries));
    }

    protected void loadClientData(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadClientData(pTag, pRegistries);
        if (pTag.contains("Inventory")) items.deserializeNBT(pRegistries, pTag.getCompound("Inventory"));
    }

    private int getContainerSize(){
        return 3;
    }

    public Component getDisplayName() {
        return Component.literal("Fluid Tank Screen");
    }

    public AbstractContainerMenu createMenu(int id, Inventory p_39955_, Player p_39956_) {
        return new TankMenu(id, p_39955_, worldPosition);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TankBE e) {
        if (!level.isClientSide) {
            ItemStack itemStack = e.getItems().getStackInSlot(0);
            IFluidHandlerItem handler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
            if (handler != null) FluidHelper.handleInventoryMaxTransfer(e.tank, handler, e.items, 0, 1);

            itemStack = e.getItems().getStackInSlot(2);
            handler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
            if (handler != null) FluidHelper.fillItemStackFromBlock(e.tank, handler, e.fillSpeed, itemStack);

            e.tank.handlePush(level, pos);
            e.tank.handlePull(level, pos);
            e.markDirty();
        }
    }

    public IItemHandler getItemHandler() {
        return holder.get();
    }
}
