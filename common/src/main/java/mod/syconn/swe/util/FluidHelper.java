package mod.syconn.swe.util;

public class FluidHelper {

    // TODO FLUID FUNCTIONS

//    public static void handleInventoryMaxTransfer(IFluidHandler blockHandler, IFluidHandlerItem itemHandler, IItemHandlerModifiable inventory, int slot1, int slot2) {
//        if (!inventory.getStackInSlot(slot1).isEmpty() && inventory.getStackInSlot(slot2).isEmpty()) {
//            boolean success;
//            ItemStack movedStack = inventory.getStackInSlot(slot1);
//            boolean isBucket = movedStack.getItem() instanceof BucketItem;
//            if (blockHandler.getFluidInTank(0).isEmpty() || itemHandler.getFluidInTank(0).getAmount() == itemHandler.getTankCapacity(0)) {
//                success = fillBlockFromItemStack(blockHandler, itemHandler, Integer.MAX_VALUE, movedStack).isSuccess();
//                movedStack = new ItemStack(Items.BUCKET);
//            } else {
//                movedStack = FluidUtil.getFilledBucket(blockHandler.getFluidInTank(0));
//                success = fillItemStackFromBlock(blockHandler, itemHandler, Integer.MAX_VALUE, movedStack).isSuccess();
//            }
//            if (success) {
//                inventory.extractItem(slot1, 1, false);
//                inventory.setStackInSlot(slot2, isBucket ? movedStack : itemHandler.getContainer());
//            }
//        }
//    }
//
//    public static FluidActionResult maxTransferStackToBlock(Level pLevel, BlockPos pPos, Direction pDir, ItemStack pStack) {
//        IFluidHandler blockHandler = FluidUtil.getFluidHandler(pLevel, pPos, pDir).orElseThrow();
//        IFluidHandler itemHandler = FluidUtil.getFluidHandler(pStack).orElseThrow();
//        return maxTransferStackToBlock(blockHandler, itemHandler, pStack);
//    }
//
//    public static FluidActionResult maxTransferStackToBlock(IFluidHandler blockHandler, IFluidHandler itemHandler, ItemStack pStack) {
//        if (blockHandler.getFluidInTank(0).isEmpty() || itemHandler.getFluidInTank(0).getAmount() == itemHandler.getTankCapacity(0)) return fillBlockFromItemStack(blockHandler, itemHandler, Integer.MAX_VALUE, pStack);
//        return fillItemStackFromBlock(blockHandler, itemHandler, Integer.MAX_VALUE, pStack);
//    }
//
//    public static FluidActionResult fillBlockFromItemStack(IFluidHandler block, IFluidHandler item, int amount, ItemStack stack) {
//        if (block.isFluidValid(0, item.getFluidInTank(0))) {
//            FluidStack fluidStack = item.drain(amount, IFluidHandler.FluidAction.SIMULATE);
//            int fill = block.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
//            item.drain(fill, IFluidHandler.FluidAction.EXECUTE);
//            return fill > 0 ? new FluidActionResult(stack) : FluidActionResult.FAILURE;
//        }
//        return FluidActionResult.FAILURE;
//    }
//
//    public static FluidActionResult fillItemStackFromBlock(IFluidHandler block, IFluidHandler item, int amount, ItemStack stack) {
//        if (item.isFluidValid(0, block.getFluidInTank(0))){
//            FluidStack fluidStack = block.drain(amount, IFluidHandler.FluidAction.SIMULATE);
//            int fill = item.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
//            block.drain(fill, IFluidHandler.FluidAction.EXECUTE);
//            return fill > 0 ? new FluidActionResult(stack) : FluidActionResult.FAILURE;
//        }
//        return FluidActionResult.FAILURE;
//    }
}
