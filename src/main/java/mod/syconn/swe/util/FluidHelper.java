package mod.syconn.swe.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import mod.syconn.swe.items.extras.ItemFluidHandler;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

/** SIMILAR TO FORGE VERSION BUT FOR {@link ItemFluidHandler} */
@Deprecated(forRemoval = true)
public class FluidHelper {

    public static ItemStack fillTankReturnStack(ItemStack stack, FluidTank tank) {
        if (stack.getItem() instanceof ItemFluidHandler handler){
            FluidStack fluid = handler.getFluid(stack);
            if (fluid.isEmpty()) return fillHandlerReturnStack(stack, tank);
            if (!tank.isFluidValid(fluid)) return stack;
            int filled = tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
            return handler.create(new FluidStack(fluid.getFluid(), fluid.getAmount() - filled));
        }
        return stack;
    }

    public static ItemStack fillHandlerReturnStack(ItemStack stack, FluidTank tank){
        return stack.getItem() instanceof ItemFluidHandler handler ? handler.create(tank.drain(handler.getSpace(stack), IFluidHandler.FluidAction.EXECUTE)) : stack;
    }

    public static ItemStack fillHandlerReturnStack(ItemStack stack, FluidTank tank, int max){
        if (stack.getItem() instanceof ItemFluidHandler handler){
            if (max > handler.getSpace(stack)) max = handler.getSpace(stack);
            FluidStack fluidStack = tank.drain(max, IFluidHandler.FluidAction.EXECUTE);
            return handler.create(new FluidStack(fluidStack.getFluid(), fluidStack.getAmount() + handler.getFluid(stack).getAmount()));
        }
        return stack;
    }

    public static void fillHandlerUpdateStack(ItemStack stack, FluidTank tank, int max){
        if (stack.getItem() instanceof ItemFluidHandler handler && (handler.getFluid(stack).isEmpty() || handler.getFluid(stack).getFluid() == tank.getFluidInTank(0).getFluid())){
            if (max > handler.getSpace(stack)) max = handler.getSpace(stack);
            handler.setAmount(stack, tank.drain(max, IFluidHandler.FluidAction.EXECUTE).getAmount() + handler.getFluid(stack).getAmount(), tank.getFluidInTank(0).getFluid());
        }
    }

    public static boolean interactWithFluidHandler(ItemStack stack, Level l, BlockPos pos, Direction side) {
        IFluidHandler tank = l.getCapability(Capabilities.FluidHandler.BLOCK, pos, side);
        if (stack.getItem() instanceof ItemFluidHandler handler) {
            if (handler.getFluid(stack).isEmpty() && !tank.getFluidInTank(0).isEmpty()) {
                handler.setFluid(stack, tank.drain(handler.getSpace(stack), IFluidHandler.FluidAction.EXECUTE));
                return true;
            } else if (!handler.getFluid(stack).isEmpty() && tank.isFluidValid(0, handler.getFluid(stack))) {
                int i = tank.fill(handler.getFluid(stack), IFluidHandler.FluidAction.EXECUTE);
                handler.setFluid(stack, new FluidStack(handler.getFluid(stack).getFluid(), handler.getFluid(stack).getAmount() - i));
                return true;
            }
        }
        return false;
    }
}
