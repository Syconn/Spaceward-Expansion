package mod.syconn.swe.services;

import mod.syconn.swe.extra.platform.services.ISingleFluidHandler;
import mod.syconn.swe.init.ComponentRegister;
import mod.syconn.swe.wrapper.ItemFluidHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class ForgeSingleFluidHandler implements ISingleFluidHandler {

    public ISingleFluidHandler get(ItemStack stack) {
        return new ForgeSingleFluidHandler(new ItemFluidHandlerWrapper(ComponentRegister.FLUID_COMPONENT, stack, 8000));
    }

    public ISingleFluidHandler get(Level level, BlockPos pos, Direction direction) {
        return new ForgeSingleFluidHandler(level.getBlockEntity(pos).getCapability(ForgeCapabilities.FLUID_HANDLER, direction).orElse(null));
    }

    public boolean has(Level level, BlockPos pos, Direction direction) {
        return level.getBlockEntity(pos).getCapability(ForgeCapabilities.FLUID_HANDLER, direction).isPresent();
    }

//    public FluidHolder getFluidInTank() {
//        return of(handler.getFluidInTank(0));
//    }
//
//    public int getTankCapacity() {
//        return handler.getTankCapacity(0);
//    }
//
//    public int fill(FluidHolder resource, FluidAction action) {
//        return handler.fill(of(resource), of(action));
//    }
//
//    public FluidHolder drain(FluidHolder resource, FluidAction action) {
//        return of(handler.drain(of(resource), of(action)));
//    }
//
//    public FluidHolder drain(int drain, FluidAction action) {
//        return of(handler.drain(drain, of(action)));
//    }
//
//    private FluidHolder of(FluidStack stack) {
//        return new FluidHolder(stack.getFluid(), stack.getAmount());
//    }
//
//    private FluidStack of(FluidHolder holder) {
//        return new FluidStack(holder.getFluid(), holder.getAmount());
//    }
//
//    private IFluidHandler.FluidAction of(FluidAction action) {
//        return switch (action) {
//            case EXECUTE -> IFluidHandler.FluidAction.EXECUTE;
//            case SIMULATE -> IFluidHandler.FluidAction.SIMULATE;
//        };
//    }
}
