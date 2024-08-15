package mod.syconn.swe.wrappers;

import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.platform.services.ISingleFluidHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class BlockFluidWrapper extends SingleVariantStorage<FluidVariant> implements FluidHandler {

    protected FluidVariant getBlankVariant() {
        return FluidVariant.blank();
    }

    protected long getCapacity(FluidVariant variant) {
        return 16000; // TODO MAY NEED TO BE CONVERTED TO FABRIC NUMBERS
    }

    protected void onFinalCommit() {
        // TODO FOR UPDATE CLIENT
    }

    public ISingleFluidHandler.FluidHolder getFluid() {
        return new ISingleFluidHandler.FluidHolder(variant.getFluid(), (int) amount);
    }

    public void setFluid(ISingleFluidHandler.FluidHolder fluidHolder) {
        variant = FluidVariant.of(fluidHolder.fluid());
        amount = fluidHolder.amount();
    }

    public void readNBT(CompoundTag nbt, HolderLookup.Provider wrapperLookup) {
        SingleVariantStorage.readNbt(this, FluidVariant.CODEC, FluidVariant::blank, nbt, wrapperLookup);
    }

    public CompoundTag writeNBT(HolderLookup.Provider wrapperLookup) {
        CompoundTag tag = new CompoundTag();
        SingleVariantStorage.writeNbt(this, FluidVariant.CODEC, tag, wrapperLookup);
        return tag;
    }
}
