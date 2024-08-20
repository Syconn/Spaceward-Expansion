package mod.syconn.swe.services;

import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.core.InteractionalFluidHandler;
import mod.syconn.swe.extra.platform.services.ISingleFluidHandler;
import mod.syconn.swe.wrappers.BlockFluidWrapper;
import mod.syconn.swe.wrappers.ComponentFluidWrapper;
import mod.syconn.swe.wrappers.InteractableFluidWrapper;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class FabricSingleFluidHandler implements ISingleFluidHandler {

    public FluidHandlerItem get(ItemStack stack) {
        return (FluidHandlerItem) ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
    }

    public boolean has(ItemStack stack) {
        return ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM) instanceof ComponentFluidWrapper;
    }

    public FluidHandler get(Level level, BlockPos pos, Direction direction) {
        return ((BlockFluidWrapper) FluidStorage.SIDED.find(level, pos, direction)).getHandler();
    }

    public boolean has(BlockGetter level, BlockPos pos, Direction direction) {
        if (level instanceof Level ext) return FluidStorage.SIDED.find(ext, pos, direction) instanceof BlockFluidWrapper;
        return false;
    }

    public InteractionalFluidHandler getInteractional(Level level, BlockPos pos, Direction direction) {
        return ((InteractableFluidWrapper) FluidStorage.SIDED.find(level, pos, direction)).getHandler();
    }

    public ItemStack getBucket(FluidHolder fluidHolder) {
        return new ItemStack(fluidHolder.getFluid().getBucket());
    }
}
