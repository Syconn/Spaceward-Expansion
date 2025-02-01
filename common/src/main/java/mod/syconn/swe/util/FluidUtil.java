package mod.syconn.swe.util;

import dev.architectury.fluid.FluidStack;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.items.FluidHolderItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class FluidUtil {

    @SuppressWarnings("ConstantConditions")
    public static boolean performInventoryTransfer(FluidHolderBlock blockHolder, Container container, int initialSlot, int depositSlot) {
        ItemStack stack = container.getItem(initialSlot);
        if (!stack.isEmpty() && container.getItem(depositSlot).isEmpty() && FluidHolderItem.hasFluidHolder(stack)) {
            FluidHolderItem itemHolder = FluidHolderItem.getFluidHolder(container, initialSlot);
            boolean success;
            boolean isBucket = stack.getItem() instanceof BucketItem;
            if (blockHolder.isEmpty() || itemHolder.getFluidStack().getAmount() == itemHandler.getTankCapacity()) {
                success = transferFluid(blockHandler, itemHandler, Integer.MAX_VALUE);
                movedStack = new ItemStack(Items.BUCKET);
            } else {
                movedStack = Services.FLUID_HANDLER.getBucket(blockHandler.getFluidHolder());
                success = pushFromBlock(blockHandler, itemHandler, Integer.MAX_VALUE);
            }
            if (success) {
                container.removeItem(slot1, 1);
                container.setItem(slot2, isBucket ? movedStack : itemHandler.getContainer());
            }
        }
        return false;
    }

    public static boolean performPlayerTransfer(Level level, BlockPos pos, BlockHitResult hitResult, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        FluidHolder holder = Services.FLUID_HANDLER.get(level, pos, hitResult.getDirection().getOpposite()).getFluidHolder();
        boolean fillBlock = FluidHelper.maxTransferStackToBlockFillBlock(level, pos, hitResult.getDirection().getOpposite(), stack);
        boolean success = FluidHelper.maxTransferStackToBlock(level, pos, hitResult.getDirection().getOpposite(), stack);
        if (success && stack.getItem() instanceof BucketItem) {
            if (fillBlock) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            else player.setItemInHand(hand, Services.FLUID_HANDLER.getBucket(holder));
        }
        return success;
    }

//    private static boolean maxTransferStackToBlockFillBlock(Level level, BlockPos pos, Direction dir, ItemStack stack) { TODO WTF ARE THESE FOR
//        FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, pos, dir);
//        FluidHandler itemHandler = Services.FLUID_HANDLER.get(stack);
//        return blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity();
//    }
//
//    private static boolean maxTransferStackToBlock(Level level, BlockPos pos, Direction dir, ItemStack stack) {
//        FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, pos, dir);
//        FluidHandler itemHandler = Services.FLUID_HANDLER.get(stack);
//        return maxTransferStackToBlock(blockHandler, itemHandler);
//    }
//
//    public static boolean maxTransferStackToBlock(FluidHandler blockHandler, FluidHandler itemHandler) {
//        if (blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity()) return fillFromItem(blockHandler, itemHandler, Integer.MAX_VALUE);
//        return fillFromBlock(blockHandler, itemHandler, Integer.MAX_VALUE);
//    }

    public static long transferFluid(@NotNull FluidHolderItem pull, @NotNull FluidHolderItem push, long amount) {
        return pull.getFluidStack().isFluidEqual(push.getFluidStack()) ? push.push(pull.pull(amount, false), false) : 0;
    }
}