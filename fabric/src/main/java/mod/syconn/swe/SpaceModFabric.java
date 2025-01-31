package mod.syconn.swe;

import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.blockentities.fabric.FluidHolderBlockImpl;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.common.items.fabric.FluidHolderItemImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;

@SuppressWarnings("UnstableApiUsage")
public final class SpaceModFabric implements ModInitializer {

    public void onInitialize() {
        FluidStorage.SIDED.registerFallback((level, pos, state, blockEntity, direction) ->
                blockEntity instanceof FluidHolderBlock.IFluidHolderBlock b ? ((FluidHolderBlockImpl.FabricFluidHolderBlock) b.getFluidHolder()).getTank() : null);

        FluidStorage.ITEM.registerFallback(((itemStack, context) -> {
            if (itemStack.getItem() instanceof FluidHolderItem.IFluidHolderItem item)
                return new FluidHolderItemImpl.FabricFluidHolderItem(itemStack, item.getCapacity());
            return null;
        }));

        SpaceMod.init();
    }
}
