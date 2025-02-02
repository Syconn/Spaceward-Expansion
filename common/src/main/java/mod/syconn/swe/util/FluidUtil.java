package mod.syconn.swe.util;

import dev.architectury.fluid.FluidStack;
import mod.syconn.swe.Constants;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.items.FluidHolderItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class FluidUtil {

    public static final String FLUID_NBT_KEY = Constants.MOD + ":Fluid";

    public static ItemStack createFluidItem(Item item, FluidStack fluidStack) {
        ItemStack container = new ItemStack(item);
        container.getOrCreateTag().put(FLUID_NBT_KEY, fluidStack.write(new CompoundTag()));
        return container;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean performInventoryTransfer(FluidHolderBlock blockHolder, Container container, int initialSlot, int depositSlot) {
        ItemStack stack = container.getItem(initialSlot);
        if (!stack.isEmpty() && container.getItem(depositSlot).isEmpty() && FluidHolderItem.hasFluidHolder(stack)) {
            FluidHolderItem itemHolder = FluidHolderItem.getFluidHolder(container, initialSlot);
            long fill = blockHolder.isEmpty() || itemHolder.isFull() ? transferFluid(itemHolder, blockHolder, Integer.MAX_VALUE) : transferFluid(blockHolder, itemHolder, Integer.MAX_VALUE);
//            boolean isBucket = stack.getItem() instanceof BucketItem; TODO BUCKET MAY NOT NEED HANDLING
//            if () {
//                fill = ;
//                movedStack = new ItemStack(Items.BUCKET);
//            } else {
//                movedStack = Services.FLUID_HANDLER.getBucket(blockHandler.getFluidHolder());
//                fill = ;
//            }
            if (fill > 0) {
                container.removeItem(initialSlot, 1);
                container.setItem(depositSlot, itemHolder.getContainer());
//                container.setItem(slot2, isBucket ? movedStack : itemHandler.getContainer());
                return true;
            }
        }
        return false;
    }

    public static boolean performPlayerTransfer(Level level, BlockPos pos, BlockHitResult hitResult, Player player, InteractionHand hand) {
        FluidHolderBlock blockHolder = FluidHolderBlock.getOrWrapFluidHolder(level, pos, hitResult.getDirection().getOpposite());
        FluidHolderItem itemHolder = FluidHolderItem.getFluidHolder(player, hand);
        long fill = transferFluid(itemHolder, blockHolder, Integer.MAX_VALUE);
        if (fill == 0) transferFluid(blockHolder, itemHolder, Integer.MAX_VALUE);
        return fill > 0;
//        boolean success = FluidHelper.maxTransferStackToBlock(level, pos, hitResult.getDirection().getOpposite(), stack);
//        if (success && stack.getItem() instanceof BucketItem) {
//            if (fillBlock) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
//            else player.setItemInHand(hand, Services.FLUID_HANDLER.getBucket(holder));
//        }
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
        return pull.getFluidStack().isFluidEqual(push.getFluidStack()) ? push.push(pull.pull(Math.min(push.getCapacity() - push.getFluidStack().getAmount(), amount), false), false) : 0;
    }
}