package mod.syconn.swe.common.blockentities.fabric;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.blockentities.InteractableFluidHolderBlock;
import mod.syconn.swe.util.FluidHolderWrapper;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class FluidHolderBlockImpl {

    public static FluidHolderBlock create(long capacity) {
        return new FabricFluidHolderBlock(capacity, null);
    }

    public static FluidHolderBlock create(long capacity, Consumer<FluidHolderBlock> onChange) {
        return new FabricFluidHolderBlock(capacity, onChange);
    }

    public static boolean hasHolder(Level level, BlockPos pos, @Nullable Direction face) {
        return FluidStorage.SIDED.find(level, pos, face) != null;
    }

    @Nullable
    public static FluidHolderBlock getOrWrapFluidHolder(Level level, BlockPos pos, @Nullable Direction face) {
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(level, pos, face);
        if (storage != null) {
            if (level.getBlockEntity(pos) instanceof FluidHolderBlock.IFluidHolderBlock block) return block.getFluidHolder();
            if (level.getBlockEntity(pos) instanceof InteractableFluidHolderBlock.IInteractableFluidHolderBlock block) return block.getFluidHolder();
            return new FluidHolderWrapper(storage);
        }
        return null;
    }

    public static class FabricFluidHolderBlock extends FluidHolderBlock {

        private final SingleFluidStorage tank;

        protected FabricFluidHolderBlock(long capacity, @Nullable Consumer<FluidHolderBlock> onChange)
        {
            this.tank = SingleFluidStorage.withFixedCapacity(capacity, () -> {
                if(onChange != null) onChange.accept(FabricFluidHolderBlock.this);
            });
        }

        public FluidStack getFluidStack() {
            return FluidStackHooksFabric.fromFabric(tank);
        }

        public long getCapacity() {
            return tank.getCapacity();
        }

        public boolean isEmpty() {
            return tank.isResourceBlank();
        }

        public void setFluidStack(FluidStack fluidStack) {
            this.tank.variant = FluidStackHooksFabric.toFabric(fluidStack);
            this.tank.amount = fluidStack.getAmount();
        }

        public long push(FluidStack fluidStack, boolean simulate) {
            try(Transaction transaction = Transaction.openOuter()) {
                long filled = this.tank.insert(FluidVariant.of(fluidStack.getFluid()), fluidStack.getAmount(), transaction);
                if(!simulate) transaction.commit();
                return filled;
            }
        }

        public FluidStack pull(long amount, boolean simulate) {
            try(Transaction transaction = Transaction.openOuter()) {
                FluidVariant variant = this.tank.getResource();
                long drained = this.tank.extract(variant, amount, transaction);
                if(!simulate) transaction.commit();
                return FluidStackHooksFabric.fromFabric(variant, drained);
            }
        }

        public void load(CompoundTag tag) {
            this.tank.readNbt(tag.getCompound("FluidTank"));
        }

        public void save(CompoundTag tag) {
            CompoundTag tankTag = new CompoundTag();
            this.tank.writeNbt(tankTag);
            tag.put("FluidTank", tankTag);
        }

        public SingleFluidStorage getTank() {
            return tank;
        }
    }
}
