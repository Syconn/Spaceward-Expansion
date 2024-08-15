package mod.syconn.swe.services;

import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.platform.services.ISingleFluidHandler;
import mod.syconn.swe.wrappers.BlockFluidWrapper;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FabricSingleFluidHandler implements ISingleFluidHandler {

    private FluidHandler handler;

    public FabricSingleFluidHandler() {}

    private FabricSingleFluidHandler(FluidHandler handler) {
        this.handler = handler;
    }

    public ISingleFluidHandler get(ItemStack stack) {
        return new FabricSingleFluidHandler((FluidHandler) ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM));
    }

    public ISingleFluidHandler get(Level level, BlockPos pos, Direction direction) {
        return new FabricSingleFluidHandler((FluidHandler) FluidStorage.SIDED.find(level, pos, direction));
    }

    public boolean has(Level level, BlockPos pos, Direction direction) {
        return FluidStorage.SIDED.find(level, pos, direction) instanceof BlockFluidWrapper;
    }

    public FluidHolder getFluidInTank() {
        return handler.getFluid();
    }

    public int getTankCapacity() {
        return handler.getCapacity();
    }

    public int fill(FluidHolder resource, FluidAction action) {
        return handler.fill(resource, action);
    }

    public FluidHolder drain(FluidHolder resource, FluidAction action) {
        return handler.drain(resource, action);
    }

    public FluidHolder drain(int drain, FluidAction action) {
        return handler.drain(drain, action);
    }
}
