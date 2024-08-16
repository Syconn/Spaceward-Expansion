package mod.syconn.swe.extra.platform.services;

import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.core.InteractionalFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISingleFluidHandler {

    FluidHandlerItem get(ItemStack stack);
    boolean has(ItemStack stack);
    FluidHandler get(Level level, BlockPos pos, Direction direction);
    InteractionalFluidHandler getInteractional(Level level, BlockPos pos, Direction direction);
    boolean has(Level level, BlockPos pos, Direction direction);
    ItemStack getBucket(FluidHolder fluidHolder);
//    boolean isFluidValid(FluidHolder fluidHolder);
//    FluidHolder getFluidInTank();
//    int getTankCapacity();
//    int fill(FluidHolder resource, FluidAction action);
//    FluidHolder drain(FluidHolder resource, FluidAction action);
//    FluidHolder drain(int drain, FluidAction action);
}
