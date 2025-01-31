package mod.syconn.swe.common.blockentities.fabric;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.blockentities.InteractableFluidHolderBlock;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class InteractableFluidHolderBlockImpl {

    public static InteractableFluidHolderBlock create(long speed, long capacity) {
        return new FabricInteractableFluidHolderBlock(speed, capacity, null);
    }

    public static InteractableFluidHolderBlock create(long speed, long capacity, Consumer<FluidHolderBlock> onChange) {
        return new FabricInteractableFluidHolderBlock(speed, capacity, onChange);
    }

    public static class FabricInteractableFluidHolderBlock extends InteractableFluidHolderBlock {

        private final SingleFluidStorage tank;

        protected FabricInteractableFluidHolderBlock(long speed, long capacity, @Nullable Consumer<FluidHolderBlock> onChange) {
            super(speed);
            this.tank = SingleFluidStorage.withFixedCapacity(capacity, () -> {
                if(onChange != null) onChange.accept(InteractableFluidHolderBlockImpl.FabricInteractableFluidHolderBlock.this);
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
