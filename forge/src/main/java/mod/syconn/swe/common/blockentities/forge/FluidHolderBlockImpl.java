package mod.syconn.swe.common.blockentities.forge;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.blockentities.InteractableFluidHolderBlock;
import mod.syconn.swe.util.FluidHolderWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FluidHolderBlockImpl {

    public static FluidHolderBlock create(long capacity) {
        return new ForgeFluidHolderBlock(capacity, null);
    }

    public static FluidHolderBlock create(long capacity, Consumer<FluidHolderBlock> onChange) {
        return new ForgeFluidHolderBlock(capacity, onChange);
    }

    public static boolean hasHolder(Level level, BlockPos pos, @Nullable Direction face) {
        return level.getBlockEntity(pos).getCapability(ForgeCapabilities.FLUID_HANDLER, face).isPresent();
    }

    @Nullable
    public static FluidHolderBlock getOrWrapFluidHolder(Level level, BlockPos pos, @Nullable Direction face) {
        Optional<IFluidHandler> fluidHandler = level.getBlockEntity(pos).getCapability(ForgeCapabilities.FLUID_HANDLER, face).resolve();
        if (fluidHandler.isPresent()) {
            if (level.getBlockEntity(pos) instanceof FluidHolderBlock.IFluidHolderBlock block) return block.getFluidHolder();
            if (level.getBlockEntity(pos) instanceof InteractableFluidHolderBlock.IInteractableFluidHolderBlock block) return block.getFluidHolder();
            return new FluidHolderWrapper(fluidHandler.get(), 0);
        }
        return null;
    }

    public static class ForgeFluidHolderBlock extends FluidHolderBlock {

        private final FluidTank tank;

        protected ForgeFluidHolderBlock(long capacity, @Nullable Consumer<FluidHolderBlock> onChange) {
            this.tank = new FluidTank((int) capacity) {
                protected void onContentsChanged() {
                    if(onChange != null) onChange.accept(ForgeFluidHolderBlock.this);
                }
            };
        }

        protected ForgeFluidHolderBlock(long capacity, @Nullable Consumer<FluidHolderBlock> onChange, Predicate<net.minecraftforge.fluids.FluidStack> validFluids) {
            this.tank = new FluidTank((int) capacity, validFluids) {
                protected void onContentsChanged() {
                    if(onChange != null) onChange.accept(ForgeFluidHolderBlock.this);
                }
            };
        }

        public FluidStack getFluidStack() {
            return FluidStackHooksForge.fromForge(tank.getFluid());
        }

        public boolean isEmpty() {
            return tank.isEmpty();
        }

        public void setFluidStack(FluidStack fluidStack) {
            this.tank.setFluid(FluidStackHooksForge.toForge(fluidStack));
        }

        public long push(FluidStack fluidStack, boolean simulate) {
            return this.tank.fill(FluidStackHooksForge.toForge(fluidStack), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        }

        public FluidStack pull(long amount, boolean simulate) {
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
