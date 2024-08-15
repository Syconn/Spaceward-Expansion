package mod.syconn.swe.blockentities;

import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.core.FluidTank;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.extra.platform.services.ISingleFluidHandler;
import mod.syconn.swe.init.BlockEntityRegister;
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

public class CanisterFillerBlockEntity extends BlockEntity { // TODO WORK WITH ALL FLUID ITEM HANDLERS

    private final int fillSpeed = 10;
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public CanisterFillerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegister.FILLER.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CanisterFillerBlockEntity e) {
        for (int i = 0; i < 4; i++) {
            if (!e.items.get(i).isEmpty()) {
                ItemStack itemStack = e.items.get(i);
                FluidHandler handler = Services.FLUID_HANDLER.get(itemStack);
                if (handler != null) {
                    FluidHolder fluidHolder = handler.getFluid();
                    if (handler.getCapacity() >= fluidHolder.getAmount() + e.fillSpeed && fluidHolder.is(Fluids.EMPTY) || fluidHolder.is(e.getFluidTank().getFluid())) {
                        FluidHolder resource = e.getFluidTank().drain(e.fillSpeed, FluidAction.EXECUTE);
                        e.getFluidTank().fill(resource.copyWith(resource.getAmount() - handler.fill(resource, FluidAction.EXECUTE)), FluidAction.EXECUTE);
                        e.update();
                    }
                }
            }
        }
    }

    public boolean addCanister(ItemStack stack) {
        FluidHandler handler = Services.FLUID_HANDLER.get(stack);
        if (handler != null && stack.getItem() instanceof Canister && handler.getFluid().is(Fluids.EMPTY) || handler.getFluid().is(getFluidTank().getFluid())) {
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
        return level.getBlockEntity(worldPosition.below(), BlockEntityRegister.TANK.get()).get().getFluidTank();
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
