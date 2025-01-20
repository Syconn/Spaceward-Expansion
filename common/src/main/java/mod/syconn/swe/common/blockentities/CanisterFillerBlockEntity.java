package mod.syconn.swe.common.blockentities;

import mod.syconn.swe.extra.core.*;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.core.ModBlockEntities;
import mod.syconn.swe.items.Canister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class CanisterFillerBlockEntity extends BlockEntity {

    private final int fillSpeed = 10;
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public CanisterFillerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.FILLER.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CanisterFillerBlockEntity e) {
        for (int i = 0; i < 4; i++) {
            if (!e.items.get(i).isEmpty()) {
                ItemStack itemStack = e.items.get(i);
                FluidHandlerItem itemHandler = Services.FLUID_HANDLER.get(itemStack);
                FluidHandler handler = Services.FLUID_HANDLER.get(level, pos.below(), Direction.UP);
                if (itemHandler != null) {
                    FluidHolder fluidHolder = itemHandler.getFluidHolder();
                    if (itemHandler.getTankCapacity() >= fluidHolder.getAmount() + e.fillSpeed && fluidHolder.is(Fluids.EMPTY) || fluidHolder.is(handler.getFluidHolder())) {
                        FluidHolder resource = handler.drain(e.fillSpeed, FluidAction.EXECUTE);
                        handler.fill(resource.copyWith(resource.getAmount() - itemHandler.fill(resource, FluidAction.EXECUTE)), FluidAction.EXECUTE);
                        e.update();
                    }
                }
            }
        }
    }

    public boolean addCanister(ItemStack stack) {
        FluidHandlerItem itemHandler = Services.FLUID_HANDLER.get(stack);
        FluidHandler handler = Services.FLUID_HANDLER.get(level, worldPosition.below(), Direction.UP);
        if (Services.FLUID_HANDLER.has(stack) && stack.getItem() instanceof Canister && itemHandler.getFluidHolder().is(Fluids.EMPTY) || itemHandler.getFluidHolder().is(handler.getFluidHolder())) {
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
