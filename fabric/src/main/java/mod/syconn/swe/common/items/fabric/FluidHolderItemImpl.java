package mod.syconn.swe.common.items.fabric;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import mod.syconn.swe.Constants;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.util.ISnapshotParticipant;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class FluidHolderItemImpl {

    public static FluidHolderItem getFluidHolder(Player player, InteractionHand hand) {
        return (FabricFluidHolderItem) ContainerItemContext.ofPlayerHand(player, hand).find(FluidStorage.ITEM);
    }

    public static FluidHolderItem getFluidHolder(Player player, @Nullable AbstractContainerMenu inventory, ItemStack stack) {
        return (FabricFluidHolderItem) ContainerItemContext.ofPlayerCursor(player, inventory).find(FluidStorage.ITEM);
    }

    public static class FabricFluidHolderItem extends FluidHolderItem implements SingleSlotStorage<FluidVariant>, ISnapshotParticipant<ResourceAmount<FluidVariant>> {
        private final String FLUID_NBT_KEY = Constants.MOD + ":Fluid";
        private final List<ResourceAmount<FluidVariant>> snapshots = new ArrayList<>();
        private final ItemStack container;
        private final long capacity;

        public FabricFluidHolderItem(ItemStack container, long capacity) {
            this.container = container;
            this.capacity = capacity;
        }

        public boolean isResourceBlank() {
            return false;
        }

        public FluidVariant getResource() {
            return FluidStackHooksFabric.toFabric(getFluidStack());
        }

        public long getAmount() {
            return getFluidStack().getAmount();
        }

        public long getCapacity() {
            return this.capacity;
        }

        public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount);
            FluidVariant variant = getResource();

            if ((resource.equals(variant) || variant.isBlank())) {
                long insertedAmount = Math.min(maxAmount, getCapacity() - getAmount());

                if (insertedAmount > 0) {
                    updateSnapshots(transaction);
                    setFluidStack(FluidStackHooksFabric.fromFabric(resource, insertedAmount));
                    return insertedAmount;
                }
            }

            return 0;
        }

        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notBlankNotNegative(resource, maxAmount);
            FluidVariant variant = getResource();

            if (resource.equals(variant)) {
                long extractedAmount = Math.min(maxAmount, getAmount());
                if (extractedAmount > 0) {
                    updateSnapshots(transaction);
                    if (getAmount() - extractedAmount == 0) setFluidStack(FluidStack.empty());
                    else setFluidStack(getFluidStack().copyWithAmount(getAmount() - extractedAmount));
                    return extractedAmount;
                }
            }

            return 0;
        }

        public List<ResourceAmount<FluidVariant>> snapshots() {
            return snapshots;
        }

        public ResourceAmount<FluidVariant> createSnapshot() {
            return new ResourceAmount<>(getResource(), getAmount());
        }

        public void readSnapshot(ResourceAmount<FluidVariant> snapshot) {
            setFluidStack(FluidStackHooksFabric.fromFabric(snapshot.resource(), snapshot.amount()));
        }

        public void releaseSnapshot(ResourceAmount<FluidVariant> snapshot) {}

        public void onFinalCommit() {}

        public FluidStack getFluidStack() {
            CompoundTag tagCompound = container.getTag();
            if (tagCompound == null || !tagCompound.contains(FLUID_NBT_KEY)) return FluidStack.empty();
            return FluidStack.read(tagCompound.getCompound(FLUID_NBT_KEY));
        }

        public void setFluidStack(FluidStack fluidStack) {
            if (!container.hasTag()) container.setTag(new CompoundTag());
            container.getTag().put(FLUID_NBT_KEY, fluidStack.write(new CompoundTag()));
        }

        public boolean isEmpty() {
            return getFluidStack().isEmpty();
        }

        public long push(FluidStack fluidStack, boolean simulate) {
            try(Transaction transaction = Transaction.openOuter()) {
                long filled = insert(FluidVariant.of(fluidStack.getFluid()), fluidStack.getAmount(), transaction);
                if(!simulate) transaction.commit();
                return filled;
            }
        }

        public FluidStack pull(long amount, boolean simulate) {
            try(Transaction transaction = Transaction.openOuter()) {
                FluidVariant variant = getResource();
                long drained = extract(variant, amount, transaction);
                if(!simulate) transaction.commit();
                return FluidStackHooksFabric.fromFabric(variant, drained);
            }
        }
    }
}
