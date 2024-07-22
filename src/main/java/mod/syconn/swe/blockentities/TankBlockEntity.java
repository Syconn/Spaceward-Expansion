package mod.syconn.swe.blockentities;

import mod.syconn.swe.Registration;
import mod.syconn.api.blockEntity.AbstractTankBE;
import mod.syconn.swe.items.extras.ItemFluidHandler;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.EmptyFluid;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TankBlockEntity extends AbstractTankBE implements MenuProvider {

    private final int fillSpeed = 500;
    private final ItemStackHandler items = new ItemStackHandler(getContainerSize()) {
        public void onContentsChanged(int slot) { markDirty(); }
    };
    private final Lazy<IItemHandler> holder = Lazy.of(() -> items);

    public TankBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.TANK.get(), pos, state, 16000);
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

    public static void serverTick(Level level, BlockPos pos, BlockState state, TankBlockEntity e) {
        if (!level.isClientSide) {
            ItemStack heldItem = e.getItems().getStackInSlot(0);
            if (heldItem.getItem() instanceof BucketItem b) {
                int fill = FluidUtil.getFluidHandler(heldItem).map(handler -> e.tank.fill(handler.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE)).orElse(0);
                if (fill > 0) {
                    e.getItems().extractItem(0, 1, false);
                    e.getItems().insertItem(1, new ItemStack(Items.BUCKET), false);
                } else if (b.content instanceof EmptyFluid && !e.tank.isEmpty() && !(e.getItems().getStackInSlot(1).getItem() instanceof BucketItem)) {
                    FluidStack fluidStack = FluidUtil.getFluidHandler(heldItem).map(handler -> e.tank.drain(handler.getTankCapacity(0), IFluidHandler.FluidAction.EXECUTE)).orElse(FluidStack.EMPTY);
                    e.getItems().extractItem(0, 1, false);
                    if (fluidStack == FluidStack.EMPTY) e.getItems().insertItem(1, new ItemStack(Items.BUCKET), false);
                    else e.getItems().insertItem(1, FluidUtil.getFilledBucket(fluidStack), false);
                }
            } else if (heldItem.getItem() instanceof ItemFluidHandler && e.getItems().getStackInSlot(1) == ItemStack.EMPTY) {
                e.getItems().extractItem(0, 1, false);
                e.getItems().insertItem(1, FluidHelper.fillTankReturnStack(heldItem, e.tank), false);
            }
            ItemStack item = e.getItems().getStackInSlot(2);
            if (item.getItem() instanceof ItemFluidHandler) {
                FluidHelper.fillHandlerUpdateStack(item, e.tank, e.fillSpeed);
            }
            e.markDirty();
        }
    }

    public IItemHandler getItemHandler() {
        return holder.get();
    }
}
