package mod.syconn.swe.common.blockentities;

import dev.architectury.fluid.FluidStack;
import mod.syconn.swe.common.items.Canister;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class CanisterFillerBlockEntity extends SyncedBE {

    private final int fillSpeed = 10;
    private final SimpleContainer container = new SimpleContainer(4);

    public CanisterFillerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.FILLER.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CanisterFillerBlockEntity e) {
        for (int i = 0; i < 4; i++) {
            if (!e.container.getItem(i).isEmpty()) {
                FluidHolderItem itemHandler = FluidHolderItem.getFluidHolder(e.container, i);
                FluidHolderBlock blockHandler = FluidHolderBlock.getOrWrapFluidHolder(level, pos.below(), Direction.UP);
                if (itemHandler != null && blockHandler != null) {
                    FluidStack fluidStack = itemHandler.getFluidStack();
                    if (fluidStack.getFluid().isSame(Fluids.EMPTY) || fluidStack.isFluidEqual(blockHandler.getFluidStack())) {
                        FluidStack pull = blockHandler.pull(e.fillSpeed, true);
                        itemHandler.push(blockHandler.pull(pull.getAmount(), false), false);
                        e.markDirty();
                    }
                }
            }
        }
    }

    public boolean addCanister(ItemStack stack) {
        if (FluidHolderItem.hasFluidHolder(stack)) {
            for (int i = 0; i < 4; i++) {
                if (container.getItem(i).isEmpty()) {
                    container.setItem(i, stack.copy());
                    markDirty();
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack removeCanister() {
        for (int i = 0; i < 4; i++) {
            if (!container.getItem(i).isEmpty()) {
                ItemStack stack = container.removeItemNoUpdate(i);
                markDirty();
                return stack;
            }
        }
        markDirty();
        return ItemStack.EMPTY;
    }

    public ItemStack getCanister(int i) {
        return container.getItem(i);
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", this.container.createTag());
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Inventory", 9)) this.container.fromTag(tag.getList("Inventory", 10));
    }
}
