package mod.syconn.swe.services;

import mod.syconn.swe.init.ComponentRegister;
import mod.syconn.swe.platform.services.ISingleFluidHandler;
import mod.syconn.swe.wrapper.ItemFluidHandlerWrapper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ForgeSingleFluidHandler implements ISingleFluidHandler {

    private final ItemFluidHandlerWrapper wrapper;

    public ForgeSingleFluidHandler(ItemFluidHandlerWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public ISingleFluidHandler get(Player player, InteractionHand hand) {
        return new ForgeSingleFluidHandler(new ItemFluidHandlerWrapper(ComponentRegister.FLUID_COMPONENT, player.getItemInHand(hand), 8000));
    }

    public FluidHolder getFluidInTank() {
        return of(wrapper.getFluidInTank(0));
    }

    public int getTankCapacity() {
        return wrapper.getTankCapacity(0);
    }

    public int fill(FluidHolder resource, FluidAction action) {
        return wrapper.fill(of(resource), of(action));
    }

    public FluidHolder drain(FluidHolder resource, FluidAction action) {
        return of(wrapper.drain(of(resource), of(action)));
    }

    public FluidHolder drain(int drain, FluidAction action) {
        return of(wrapper.drain(drain, of(action)));
    }

    private FluidHolder of(FluidStack stack) {
        return new FluidHolder(stack.getFluid(), stack.getAmount());
    }

    private FluidStack of(FluidHolder holder) {
        return new FluidStack(holder.fluid(), holder.amount());
    }

    private IFluidHandler.FluidAction of(FluidAction action) {
        return switch (action) {
            case EXECUTE -> IFluidHandler.FluidAction.EXECUTE;
            case SIMULATE -> IFluidHandler.FluidAction.SIMULATE;
        };
    }
}
