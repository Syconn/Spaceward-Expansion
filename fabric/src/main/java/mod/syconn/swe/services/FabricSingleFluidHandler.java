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

    public boolean has(Level level, BlockPos pos, Direction direction) {
        return FluidStorage.SIDED.find(level, pos, direction) instanceof BlockFluidWrapper;
    }

    public InteractionalFluidHandler getInteractional(Level level, BlockPos pos, Direction direction) {
        return ((InteractableFluidWrapper) FluidStorage.SIDED.find(level, pos, direction)).getHandler();
    }

    public ItemStack getBucket(FluidHolder fluidHolder) {
        return new ItemStack(fluidHolder.getFluid().getBucket());
    }

    //    public FluidHolder getFluidInTank() {
//        return handler.getFluid();
//    }
//
//    public int getTankCapacity() {
//        return handler.getCapacity();
//    }
//
//    public int fill(FluidHolder resource, FluidAction action) {
//        return handler.fill(resource, action);
//    }
//
//    public FluidHolder drain(FluidHolder resource, FluidAction action) {
//        return handler.drain(resource, action);
//    }
//
//    public FluidHolder drain(int drain, FluidAction action) {
//        return handler.drain(drain, action);
//    }
}
