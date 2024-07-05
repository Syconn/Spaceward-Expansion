package mod.syconn.swe.blockentities;

import net.minecraft.core.BlockPos;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import mod.syconn.swe.items.Canister;

public class CanisterFillerBlockEntity extends BlockEntity {

    // TODO O2 tanks work when no space suit + render (Shouldnt)
    private int fillSpeed = 10;
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public CanisterFillerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntity.FILLER.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CanisterFillerBlockEntity e) {
        for (int i = 0; i < 4; i++) {
            if (!e.items.get(i).isEmpty()) {
                ItemStack item = e.items.get(i);
                if (Canister.getType(item) == Fluids.EMPTY || Canister.getType(item) == e.getFluidTank().getFluid().getFluid()) {
                    if (Canister.getMaxValue(item) > Canister.getValue(item)) {
                        FluidStack fill = e.getFluidTank().drain(e.fillSpeed, IFluidHandler.FluidAction.SIMULATE);
                        if (fill.getAmount() > Canister.getMaxValue(item) - Canister.getValue(item)) {
                            fill.setAmount(Canister.getMaxValue(item) - Canister.getValue(item));
                        }
                        e.getFluidTank().drain(fill, IFluidHandler.FluidAction.EXECUTE);
                        Canister.increaseFluid(item, fill);
                        e.update();
                    }
                }
            }
        }
    }

    public boolean addCanister(ItemStack stack) {
        if (stack.getItem() instanceof Canister c && (Canister.getType(stack) == Fluids.EMPTY || c.getFluid(stack).getFluid() == getFluidTank().getFluid().getFluid())) {
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
        return level.getBlockEntity(worldPosition.below(), ModBlockEntity.TANK.get()).get().getFluidTank();
    }

    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        ContainerHelper.saveAllItems(p_187471_, this.items);
    }

    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        this.items = NonNullList.withSize(4, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(p_155245_, this.items);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        ContainerHelper.saveAllItems(tag, this.items);
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
