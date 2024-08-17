package mod.syconn.swe.extra.helpers;

import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class FluidHelper {

    // TODO FLUID FUNCTIONS

    public static void handleInventoryMaxTransfer(FluidHandler blockHandler, FluidHandlerItem itemHandler, Container inventory, int slot1, int slot2) {
        if (!inventory.getItem(slot1).isEmpty() && inventory.getItem(slot2).isEmpty()) {
            boolean success;
            ItemStack movedStack = inventory.getItem(slot1);
            boolean isBucket = movedStack.getItem() instanceof BucketItem;
            if (blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity()) {
                success = fillBlockFromItemStack(blockHandler, itemHandler, Integer.MAX_VALUE);
                movedStack = new ItemStack(Items.BUCKET);
            } else {
                movedStack = Services.FLUID_HANDLER.getBucket(blockHandler.getFluidHolder());
                success = fillItemStackFromBlock(blockHandler, itemHandler, Integer.MAX_VALUE);
            }
            if (success) {
                inventory.removeItem(slot1, 1);
                inventory.setItem(slot2, isBucket ? movedStack : itemHandler.getContainer());
            }
        }
    }

    public static boolean maxTransferStackToBlock(Level level, BlockPos pos, Direction dir, ItemStack stack) {
        FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, pos, dir);
        FluidHandler itemHandler = Services.FLUID_HANDLER.get(stack);
        return maxTransferStackToBlock(blockHandler, itemHandler);
    }

    public static boolean maxTransferStackToBlock(FluidHandler blockHandler, FluidHandler itemHandler) {
        if (blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity()) return fillBlockFromItemStack(blockHandler, itemHandler, Integer.MAX_VALUE);
        return fillItemStackFromBlock(blockHandler, itemHandler, Integer.MAX_VALUE);
    }

    public static boolean fillBlockFromItemStack(FluidHandler block, FluidHandler item, int amount) {
        if (block.isFluidValid(item.getFluidHolder())) {
            FluidHolder fluidHolder = item.drain(amount, FluidAction.SIMULATE);
            int fill = block.fill(fluidHolder, FluidAction.EXECUTE);
            item.drain(fill, FluidAction.EXECUTE);
            return fill > 0;
        }
        return false;
    }

    public static boolean fillItemStackFromBlock(FluidHandler block, FluidHandler item, int amount) {
        if (item.isFluidValid(block.getFluidHolder())){
            FluidHolder fluidHolder = block.drain(amount, FluidAction.SIMULATE);
            int fill = item.fill(fluidHolder, FluidAction.EXECUTE);
            block.drain(fill, FluidAction.EXECUTE);
            return fill > 0;
        }
        return false;
    }
}
