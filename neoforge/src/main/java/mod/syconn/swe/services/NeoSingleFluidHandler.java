package mod.syconn.swe.services;

import mod.syconn.swe.data.capability.APICapabilities;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.core.InteractionalFluidHandler;
import mod.syconn.swe.extra.platform.services.ISingleFluidHandler;
import mod.syconn.swe.helper.NeoFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;

public class NeoSingleFluidHandler implements ISingleFluidHandler { // TODO Maybe have ItemHandler Be held in Wrapper then get from Wrapper

    public FluidHandlerItem get(ItemStack stack) {
        return new NeoFluidHandler.ItemFluidHandler(stack.getCapability(Capabilities.FluidHandler.ITEM));
    }

    public boolean has(ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
    }

    public FluidHandler get(Level level, BlockPos pos, Direction direction) {
        return new NeoFluidHandler.BlockFluidHandler(level.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction));
    }

    public boolean has(Level level, BlockPos pos, Direction direction) {
        return level.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction) != null;
    }

    public InteractionalFluidHandler getInteractional(Level level, BlockPos pos, Direction direction) {
        return level.getCapability(APICapabilities.FluidHandler.BLOCK, pos, direction);
    }

    public ItemStack getBucket(FluidHolder fluidHolder) {
        return FluidUtil.getFilledBucket(new FluidStack(fluidHolder.getFluid(), fluidHolder.getAmount()));
    }

    //    public FluidHolder getFluidInTank() { TODO REMOVE OR RE-IMPLEMENT
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
