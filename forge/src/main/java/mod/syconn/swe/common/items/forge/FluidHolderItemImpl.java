package mod.syconn.swe.common.items.forge;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import mod.syconn.swe.Constants;
import mod.syconn.swe.common.items.FluidHolderItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FluidHolderItemImpl {

    public static FluidHolderItem getFluidHolder(Player player, InteractionHand hand) {
        return (FluidHolderItem) player.getItemInHand(hand).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseThrow(RuntimeException::new);
    }

    public static FluidHolderItem getFluidHolder(Player player, @Nullable AbstractContainerMenu inventory, ItemStack stack) {
        return (FluidHolderItem) stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseThrow(RuntimeException::new);
    }

    ///  Clone of {@link FluidHandlerItemStack} for FluidHolders
    public static class ForgeFluidHolderItem extends FluidHolderItem implements IFluidHandlerItem, ICapabilityProvider {

        private final String FLUID_NBT_KEY = Constants.MOD + ":Fluid";
        private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);
        private final ItemStack container;
        private final int capacity;
        private final Predicate<FluidStack> validFluids;

        public ForgeFluidHolderItem(ItemStack container, int capacity) {
            this.container = container;
            this.capacity = capacity;
            this.validFluids = (fluidStack -> true);
        }

        public ForgeFluidHolderItem(ItemStack container, int capacity, Predicate<FluidStack> validFluids) {
            this.container = container;
            this.capacity = capacity;
            this.validFluids = validFluids;
        }

        public @NotNull ItemStack getContainer() {
            return container;
        }

        public FluidStack getFluid() {
            CompoundTag tagCompound = container.getTag();
            if (tagCompound == null || !tagCompound.contains(FLUID_NBT_KEY)) return FluidStack.EMPTY;
            return FluidStack.loadFluidStackFromNBT(tagCompound.getCompound(FLUID_NBT_KEY));
        }

        protected void setFluid(FluidStack fluid) {
            if (!container.hasTag()) container.setTag(new CompoundTag());
            CompoundTag fluidTag = new CompoundTag();
            fluid.writeToNBT(fluidTag);
            container.getTag().put(FLUID_NBT_KEY, fluidTag);
        }

        public int getTanks() {
            return 1;
        }

        public @NotNull FluidStack getFluidInTank(int tank) {
            return getFluid();
        }

        public int getTankCapacity(int tank) {
            return capacity;
        }

        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return validFluids.test(stack);
        }

        private boolean canFillFluidType(FluidStack resource) {
            return this.isEmpty() || this.getFluid().isFluidEqual(resource);
        }

        public int fill(FluidStack resource, FluidAction doFill) {
            if (container.getCount() != 1 || resource.isEmpty() || !canFillFluidType(resource)) return 0;

            FluidStack contained = getFluid();
            if (contained.isFluidEqual(resource) || contained.isEmpty()) {
                final int fillAmount = Math.min(capacity - contained.getAmount(), resource.getAmount());
                if (doFill.execute() && fillAmount > 0) setFluid(new FluidStack(resource.getFluid(), fillAmount));
                return fillAmount;
            }

            return 0;
        }

        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (container.getCount() != 1 || resource.isEmpty() || !resource.isFluidEqual(getFluid())) return FluidStack.EMPTY;
            return drain(resource.getAmount(), action);
        }

        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (container.getCount() != 1 || getFluid().isEmpty() || maxDrain <= 0) return FluidStack.EMPTY;
            FluidStack contained = getFluid();
            final int drainAmount = Math.min(contained.getAmount(), maxDrain);

            FluidStack drained = contained.copy();
            drained.setAmount(drainAmount);
            if (action.execute()) {
                contained.shrink(drainAmount);
                if (contained.isEmpty()) setFluid(FluidStack.EMPTY);
                else setFluid(contained);
            }

            return drained;
        }

        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
            return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, holder);
        }

        public dev.architectury.fluid.FluidStack getFluidStack() {
            return FluidStackHooksForge.fromForge(getFluid());
        }

        public boolean isEmpty() {
            return getFluid().isEmpty();
        }

        public void setFluidStack(dev.architectury.fluid.FluidStack fluidStack) {
            setFluid(FluidStackHooksForge.toForge(fluidStack));
        }

        public long push(dev.architectury.fluid.FluidStack fluidStack, boolean simulate) {
            return fill(FluidStackHooksForge.toForge(fluidStack), simulate ? FluidAction.SIMULATE : FluidAction.EXECUTE);
        }

        public dev.architectury.fluid.FluidStack pull(long amount, boolean simulate) {
            return FluidStackHooksForge.fromForge(drain((int) amount, simulate ? FluidAction.SIMULATE : FluidAction.EXECUTE));
        }
    }
}
