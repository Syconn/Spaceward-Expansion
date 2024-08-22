package mod.syconn.swe.services;

import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.extra.platform.services.IFluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;

public class NeoFluidHelper implements IFluidHelper {

    public void handleInventoryMaxTransfer(FluidHandler blockHandler, ItemStack itemStack, Container inventory, int slot1, int slot2, BlockEntity be) {
        if (Services.FLUID_HANDLER.has(itemStack) && !inventory.getItem(slot1).isEmpty() && inventory.getItem(slot2).isEmpty()) {
            FluidHandlerItem itemHandler = Services.FLUID_HANDLER.get(itemStack);
            ItemStack movedStack = inventory.getItem(slot1);
            boolean success, isBucket = movedStack.getItem() instanceof BucketItem;
            if (blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity()) {
                success = fillBlockFromItemStack(blockHandler, itemHandler.getContainer(), Integer.MAX_VALUE);
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

    public boolean interactWithBlock(Level level, BlockPos pos, BlockHitResult hitResult, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        FluidHolder holder = Services.FLUID_HANDLER.get(level, pos, hitResult.getDirection().getOpposite()).getFluidHolder();
        boolean fillBlock = maxTransferStackToBlockFillBlock(level, pos, hitResult.getDirection().getOpposite(), stack);
        boolean success = maxTransferStackToBlock(level, pos, hitResult.getDirection().getOpposite(), stack);
        if (success && stack.getItem() instanceof BucketItem) {
            if (fillBlock) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            else player.setItemInHand(hand, Services.FLUID_HANDLER.getBucket(holder));
        }
        return success;
    }

    public boolean maxTransferStackToBlockFillBlock(Level level, BlockPos pos, Direction dir, ItemStack stack) {
        FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, pos, dir);
        FluidHandler itemHandler = Services.FLUID_HANDLER.get(stack);
        return blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity();
    }

    public boolean maxTransferStackToBlock(Level level, BlockPos pos, Direction dir, ItemStack stack) {
        FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, pos, dir);
        FluidHandlerItem itemHandler = Services.FLUID_HANDLER.get(stack);
        return maxTransferStackToBlock(blockHandler, itemHandler);
    }

    public boolean maxTransferStackToBlock(FluidHandler blockHandler, FluidHandlerItem itemHandler) {
        if (blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity()) return fillBlockFromItemStack(blockHandler, itemHandler.getContainer(), Integer.MAX_VALUE);
        return fillItemStackFromBlock(blockHandler, itemHandler, Integer.MAX_VALUE);
    }

    public boolean fillBlockFromItemStack(FluidHandler block, ItemStack itemStack, int amount) {
        FluidHandlerItem item = Services.FLUID_HANDLER.get(itemStack);
        if (Services.FLUID_HANDLER.has(itemStack) && block.isFluidValid(item.getFluidHolder())) {
            FluidHolder fluidHolder = item.drain(amount, FluidAction.SIMULATE);
            int fill = block.fill(fluidHolder, FluidAction.EXECUTE);
            item.drain(fill, FluidAction.EXECUTE);
            return fill > 0;
        }
        return false;
    }

    public boolean fillItemStackFromBlock(BlockEntity be, FluidHandler block, ItemStack itemStack, int amount, int slot) {
        return fillItemStackFromBlock(block, Services.FLUID_HANDLER.get(itemStack), amount);
    }

    public boolean fillItemStackFromBlock(FluidHandler block, FluidHandlerItem itemHandler, int amount) {
        if (itemHandler.isFluidValid(block.getFluidHolder())){
            FluidHolder fluidHolder = block.drain(amount, FluidAction.SIMULATE);
            int fill = itemHandler.fill(fluidHolder, FluidAction.EXECUTE);
            block.drain(fill, FluidAction.EXECUTE);
            return fill > 0;
        }
        return false;
    }
}
