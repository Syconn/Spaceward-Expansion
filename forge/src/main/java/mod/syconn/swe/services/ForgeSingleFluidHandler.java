package mod.syconn.swe.services;

import mod.syconn.swe.data.capability.APICapabilities;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.core.InteractionalFluidHandler;
import mod.syconn.swe.extra.platform.services.ISingleFluidHandler;
import mod.syconn.swe.helper.ForgeFluidHandler;
import mod.syconn.swe.init.CommonTags;
import mod.syconn.swe.wrapper.ItemFluidHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class ForgeSingleFluidHandler implements ISingleFluidHandler {

    public FluidHandlerItem get(ItemStack stack) {
        return new ForgeFluidHandler.ItemFluidHandler(new ItemFluidHandlerWrapper(stack, 8000));
    }

    public boolean has(ItemStack stack) {
        return stack.is(CommonTags.CANISTERS);
    }

    public FluidHandler get(Level level, BlockPos pos, Direction direction) {
        return new ForgeFluidHandler.BlockFluidHandler(level.getBlockEntity(pos).getCapability(ForgeCapabilities.FLUID_HANDLER, direction).orElse(null));
    }

    public boolean has(Level level, BlockPos pos, Direction direction) {
        return level.getBlockEntity(pos).getCapability(ForgeCapabilities.FLUID_HANDLER, direction).isPresent();
    }

    public InteractionalFluidHandler getInteractional(Level level, BlockPos pos, Direction direction) {
        return level.getBlockEntity(pos).getCapability(APICapabilities.INTERACTIONAL_HANDLER, direction).orElse(null);
    }

    public ItemStack getBucket(FluidHolder fluidHolder) {
        return FluidUtil.getFilledBucket(new FluidStack(fluidHolder.getFluid(), 1));
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
