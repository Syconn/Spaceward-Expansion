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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;

public class NeoSingleFluidHandler implements ISingleFluidHandler {

    public FluidHandlerItem get(ItemStack stack) {
        return new NeoFluidHandler.ItemFluidHandler(stack.getCapability(Capabilities.FluidHandler.ITEM));
    }

    public boolean has(ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
    }

    public FluidHandler get(Level level, BlockPos pos, Direction direction) {
        return new NeoFluidHandler.BlockFluidHandler(level.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction));
    }

    public boolean has(BlockGetter level, BlockPos pos, Direction direction) {
        if (level instanceof ILevelExtension ext) return ext.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction) != null;
        return false;
    }

    public InteractionalFluidHandler getInteractional(Level level, BlockPos pos, Direction direction) {
        return level.getCapability(APICapabilities.FluidHandler.BLOCK, pos, direction);
    }

    public ItemStack getBucket(FluidHolder fluidHolder) {
        return FluidUtil.getFilledBucket(new FluidStack(fluidHolder.getFluid(), fluidHolder.getAmount()));
    }
}
