package mod.syconn.swe.common.blockentities.forge;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.blockentities.InteractableFluidHolderBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class InteractableFluidHolderBlockImpl {

    public static InteractableFluidHolderBlock create(long speed, long capacity) {
        return new ForgeInteractableFluidHolderBlock(speed, capacity, null, fluidStack -> true);
    }

    public static InteractableFluidHolderBlock create(long speed, long capacity, Consumer<FluidHolderBlock> onChange) {
        return new ForgeInteractableFluidHolderBlock(speed, capacity, onChange, fluidStack -> true);
    }

    public static class ForgeInteractableFluidHolderBlock extends InteractableFluidHolderBlock {

        private final FluidTank tank;

        protected ForgeInteractableFluidHolderBlock(long speed, long capacity, @Nullable Consumer<FluidHolderBlock> onChange) {
            super(speed);
            this.tank = new FluidTank((int) capacity) {
                protected void onContentsChanged() {
                    if(onChange != null) onChange.accept(InteractableFluidHolderBlockImpl.ForgeInteractableFluidHolderBlock.this);
                }
            };
        }

        protected ForgeInteractableFluidHolderBlock(long speed, long capacity, @Nullable Consumer<FluidHolderBlock> onChange, Predicate<FluidStack> validFluids) {
            super(speed);
            this.tank = new FluidTank((int) capacity, validFluids) {
                protected void onContentsChanged() {
                    if(onChange != null) onChange.accept(InteractableFluidHolderBlockImpl.ForgeInteractableFluidHolderBlock.this);
                }
            };
        }

        public dev.architectury.fluid.FluidStack getFluidStack() {
            return FluidStackHooksForge.fromForge(tank.getFluid());
        }

        public long getCapacity() {
            return tank.getCapacity();
        }

        public boolean isEmpty() {
            return tank.isEmpty();
        }

        public void setFluidStack(dev.architectury.fluid.FluidStack fluidStack) {
            this.tank.setFluid(FluidStackHooksForge.toForge(fluidStack));
        }

        public long push(dev.architectury.fluid.FluidStack fluidStack, boolean simulate) {
            return this.tank.fill(FluidStackHooksForge.toForge(fluidStack), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        }

        public dev.architectury.fluid.FluidStack pull(long amount, boolean simulate) {
            return FluidStackHooksForge.fromForge(this.tank.drain((int) amount, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
        }

        public void load(CompoundTag tag) {
            this.tank.readFromNBT(tag.getCompound("FluidTank"));
        }

        public void save(CompoundTag tag) {
            CompoundTag tankTag = new CompoundTag();
            this.tank.writeToNBT(tankTag);
            tag.put("FluidTank", tankTag);
        }

        public FluidTank getTank() {
            return this.tank;
        }
    }
}
