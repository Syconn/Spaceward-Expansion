package mod.syconn.swe.blockentities;

import mod.syconn.swe.Registration;
import mod.syconn.swe.items.Canister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class CanisterFillerBlockEntity extends BlockEntity { // TODO WORK WITH ALL FLUID ITEM HANDLERS

    // TODO O2 tanks work when no space suit + render (Shouldnt)
    private final int fillSpeed = 10;
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public CanisterFillerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(Registration.FILLER.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CanisterFillerBlockEntity e) {
        for (int i = 0; i < 4; i++) {
            if (!e.items.get(i).isEmpty()) {
                ItemStack itemStack = e.items.get(i);
                IFluidHandlerItem handler = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
                if (handler != null) {
                    FluidStack fluidStack = handler.getFluidInTank(0);
                    if (handler.getTankCapacity(0) >= fluidStack.getAmount() + e.fillSpeed && fluidStack.is(Fluids.EMPTY) || FluidStack.isSameFluid(fluidStack, e.getFluidTank().getFluid())) {
                        FluidStack resource = e.getFluidTank().drain(e.fillSpeed, IFluidHandler.FluidAction.EXECUTE);
                        e.getFluidTank().fill(resource.copyWithAmount(resource.getAmount() - handler.fill(resource, IFluidHandler.FluidAction.EXECUTE)), IFluidHandler.FluidAction.EXECUTE);
                        e.update();
                    }
                }
            }
        }
    }

    public boolean addCanister(ItemStack stack) {
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (handler != null && stack.getItem() instanceof Canister && handler.getFluidInTank(0).is(Fluids.EMPTY) || FluidStack.isSameFluid(handler.getFluidInTank(0), getFluidTank().getFluidInTank(0))) {
            for (int i = 0; i < 4; i++) {
                if (items.get(i).isEmpty()) {
                    items.set(i, stack.copy());
                    update();
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack removeCanister() {
        for (int i = 0; i < 4; i++) {
            if (!items.get(i).isEmpty()) {
                ItemStack stack = items.set(i, ItemStack.EMPTY);
                update();
                return stack;
            }
        }
        update();
        return ItemStack.EMPTY;
    }

    public ItemStack getCanister(int i) {
        return items.get(i);
    }

    public FluidTank getFluidTank() {
        return level.getBlockEntity(worldPosition.below(), Registration.TANK.get()).get().getFluidTank();
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        ContainerHelper.saveAllItems(pTag, this.items, pRegistries);
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        this.items = NonNullList.withSize(4, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items, pRegistries);
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        ContainerHelper.saveAllItems(tag, this.items, pRegistries);
        return tag;
    }

    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected void update(){
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
    }
}
