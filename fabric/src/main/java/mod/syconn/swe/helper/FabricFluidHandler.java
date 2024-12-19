package mod.syconn.swe.helper;

import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.data.components.FluidHolderComponent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;

public class FabricFluidHandler {

    public static class BlockFluidHandler implements FluidHandler {

        private final Storage<FluidVariant> storage;
        private final StorageView<FluidVariant> view;

        public BlockFluidHandler(Storage<FluidVariant> storage) {
            this.storage = storage;
            this.view = storage.iterator().next();
        }

        public FluidHolder getFluidHolder() {
            return of(view.getResource(), (int) view.getAmount());
        }

        public int getTankCapacity() {
            return (int) view.getCapacity();
        }

        public void setFluid(FluidHolder fluidHolder) {
            onContentsChanged();
        }

        public boolean isFluidValid(FluidHolder holder) {
            return true;
        }

        public void onContentsChanged() {}

        public int fill(FluidHolder resource, FluidAction doFill) {
            try (Transaction transaction = Transaction.openOuter()) {
                int value = (int) storage.insert(of(resource), resource.getAmount(), transaction);
                if (doFill == FluidAction.EXECUTE) {
                    transaction.commit();
                } else {
                    transaction.abort();
                }
                return value;
            }
        }

        public FluidHolder drain(FluidHolder resource, FluidAction action) {
            try (Transaction transaction = Transaction.openOuter()) {
                int value = (int) storage.extract(of(resource), resource.getAmount(), transaction);
                if (action == FluidAction.EXECUTE) {
                    transaction.commit();
                } else {
                    transaction.abort();
                }
                return new FluidHolder(resource.getFluid(), value);
            }
        }

        public FluidHolder drain(int maxDrain, FluidAction action) {
            return drain(new FluidHolder(getFluidHolder().getFluid(), maxDrain), action);
        }
    }

    public static class ItemFluidHandler implements FluidHandlerItem {

        private final Storage<FluidVariant> storage;
        private final StorageView<FluidVariant> view;
        private final ItemStack holder;

        public ItemFluidHandler(Storage<FluidVariant> storage, ItemStack holder) {
            this.storage = storage;
            this.holder = holder;
            this.view = storage.iterator().next();
        }

        public FluidHolder getFluidHolder() {
            return of(view.getResource(), (int) view.getAmount());
        }

        public int getTankCapacity() {
            return (int) view.getCapacity();
        }

        public void setFluid(FluidHolder fluidHolder) {
            onContentsChanged();
        }

        public boolean isFluidValid(FluidHolder holder) {
            return true;
        }

        public void onContentsChanged() {}

        public int fill(FluidHolder resource, FluidAction doFill) {
            try (Transaction transaction = Transaction.openOuter()) {
                int value = (int) storage.insert(of(resource), resource.getAmount(), transaction);
                if (doFill == FluidAction.EXECUTE) {
                    transaction.commit();
                } else {
                    transaction.abort();
                }
                return value;
            }
        }

        public FluidHolder drain(FluidHolder resource, FluidAction action) {
            try (Transaction transaction = Transaction.openOuter()) {
                int value = (int) storage.extract(of(resource), resource.getAmount(), transaction);
                if (action == FluidAction.EXECUTE) {
                    transaction.commit();
                } else {
                    transaction.abort();
                }
                return new FluidHolder(resource.getFluid(), value);
            }
        }

        public FluidHolder drain(int maxDrain, FluidAction action) {
            return drain(new FluidHolder(getFluidHolder().getFluid(), maxDrain), action);
        }

        public ItemStack getContainer() {
            FluidHolderComponent.updateFluidHolder(holder, getFluidHolder());
            return holder;
        }
    }

    private static FluidHolder of(FluidVariant variant, int amount) {
        return new FluidHolder(variant.getFluid(), amount);
    }

    private static FluidVariant of(FluidHolder holder) {
        return FluidVariant.of(holder.getFluid());
    }
}
