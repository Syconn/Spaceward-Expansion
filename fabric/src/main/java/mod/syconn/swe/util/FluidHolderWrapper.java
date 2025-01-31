package mod.syconn.swe.util;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import dev.architectury.platform.Platform;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;

@SuppressWarnings("UnstableApiUsage")
public class FluidHolderWrapper extends FluidHolderBlock {

    private final Storage<FluidVariant> storage;
    private final StorageView<FluidVariant> storageView;

    public FluidHolderWrapper(Storage<FluidVariant> storage) {
        this.storage = storage;
        this.storageView = storage.iterator().next();
    }

    public FluidStack getFluidStack() {
        return FluidStackHooksFabric.fromFabric(this.storageView);
    }

    public boolean isEmpty() {
        return this.storageView.isResourceBlank();
    }

    public long push(FluidStack fluidStack, boolean simulate) {
        try(Transaction transaction = Transaction.openOuter()) {
            long filled = this.storage.insert(FluidVariant.of(fluidStack.getFluid()), fluidStack.getAmount(), transaction);
            if(!simulate) transaction.commit();
            return filled;
        }
    }

    public FluidStack pull(long amount, boolean simulate) {
        try(Transaction transaction = Transaction.openOuter()) {
            FluidVariant variant = this.storageView.getResource();
            long drained = this.storage.extract(variant, amount, transaction);
            if(!simulate) transaction.commit();
            return FluidStackHooksFabric.fromFabric(variant, drained);
        }
    }

    public void setFluidStack(FluidStack fluidStack) {
        if (Platform.isDevelopmentEnvironment()) throw new AssertionError("Unexpected Result"); // TODO REMOVE IF NOT NEEDED :)
    }

    public void load(CompoundTag tag) { }

    public void save(CompoundTag tag) { }
}
