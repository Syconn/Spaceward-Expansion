package mod.syconn.swe.services;

import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandler;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.core.FluidHolder;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.extra.platform.services.IFluidHelper;
import mod.syconn.swe.helper.FabricFluidHandler;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
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

public class FabricFluidHelper implements IFluidHelper {

    public void handleInventoryMaxTransfer(FluidHandler blockHandler, ItemStack itemStack, Container inventory, int slot1, int slot2, BlockEntity be) {
        if (Services.FLUID_HANDLER.has(itemStack) && !inventory.getItem(slot1).isEmpty() && inventory.getItem(slot2).isEmpty()) {
            ItemStack movedStack = inventory.getItem(slot1);
            boolean success = false, isBucket = movedStack.getItem() instanceof BucketItem;
            FluidHandlerItem itemHandler = getFabricHandler(be.getLevel(), be.getBlockPos(), null, slot1);
            if (!(movedStack.getItem() instanceof BucketItem)) {
                if (blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity()) {
                    success = fillBlockFromItemStack(blockHandler, itemHandler, Integer.MAX_VALUE);
                } else {
                    success = fillItemStackFromBlock(blockHandler, itemHandler, Integer.MAX_VALUE);
                }
            } else {
                FluidHolder holder = itemHandler.getFluidHolder();
                if (blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity() && !holder.isEmpty()) {
                    int fill = blockHandler.fill(holder.copyWith(1000), FluidAction.EXECUTE);
                    if (fill == 1000) success = true;
                    else blockHandler.drain(1000, FluidAction.EXECUTE);
                    movedStack = new ItemStack(Items.BUCKET);
                } else if (holder.isEmpty() && blockHandler.getFluidHolder().getAmount() >= 1000) {
                    movedStack = Services.FLUID_HANDLER.getBucket(blockHandler.getFluidHolder());
                    blockHandler.drain(1000, FluidAction.EXECUTE);
                    success = true;
                }
            }
            if (success) {
                inventory.removeItem(slot1, 1);
                inventory.setItem(slot2, isBucket ? movedStack : itemHandler.getContainer());
            }
        }
    }

    public boolean interactWithBlock(Level level, BlockPos pos, BlockHitResult hitResult, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        FluidHandlerItem itemHandler = getFabricHandler(player, hand);
        boolean success = false;
        if (!(stack.getItem() instanceof BucketItem)) {
            FluidHolder holder = Services.FLUID_HANDLER.get(level, pos, hitResult.getDirection().getOpposite()).getFluidHolder();
            boolean fillBlock = maxTransferStackToBlockFillBlock(level, pos, hitResult.getDirection().getOpposite(), itemHandler);
            success = maxTransferStackToBlock(level, pos, hitResult.getDirection().getOpposite(), itemHandler);
            if (success && stack.getItem() instanceof BucketItem) {
                if (fillBlock) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                else player.setItemInHand(hand, Services.FLUID_HANDLER.getBucket(holder));
            }
        } else {
            FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, pos, hitResult.getDirection().getOpposite());
            FluidHolder holder = itemHandler.getFluidHolder();
            if (blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity() && !holder.isEmpty()) {
                int fill = blockHandler.fill(holder.copyWith(1000), FluidAction.EXECUTE);
                if (fill == 1000) success = true;
                else blockHandler.drain(1000, FluidAction.EXECUTE);
                player.setItemInHand(hand,  new ItemStack(Items.BUCKET));
            } else if (holder.isEmpty() && blockHandler.getFluidHolder().getAmount() >= 1000) {
                player.setItemInHand(hand, Services.FLUID_HANDLER.getBucket(blockHandler.getFluidHolder()));
                blockHandler.drain(1000, FluidAction.EXECUTE);
                success = true;
            }
        }
        return success;
    }

    public boolean maxTransferStackToBlockFillBlock(Level level, BlockPos pos, Direction dir, FluidHandlerItem itemHandler) {
        FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, pos, dir);
        return blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity();
    }

    public boolean maxTransferStackToBlock(Level level, BlockPos pos, Direction dir, FluidHandlerItem itemHandler) {
        FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, pos, dir);
        return maxTransferStackToBlock(blockHandler, itemHandler);
    }

    public boolean maxTransferStackToBlock(FluidHandler blockHandler, FluidHandlerItem itemHandler) {
        if (blockHandler.getFluidHolder().isEmpty() || itemHandler.getFluidHolder().getAmount() == itemHandler.getTankCapacity()) return fillBlockFromItemStack(blockHandler, itemHandler, Integer.MAX_VALUE);
        return fillItemStackFromBlock(blockHandler, itemHandler, Integer.MAX_VALUE);
    }

    public boolean fillBlockFromItemStack(FluidHandler block, FluidHandlerItem itemHandler, int amount) {
        if (block.isFluidValid(itemHandler.getFluidHolder())) {
            FluidHolder fluidHolder = itemHandler.drain(amount, FluidAction.EXECUTE);
            itemHandler.fill(fluidHolder, FluidAction.EXECUTE); // FABRIC LACKS SIM FUNCTIONS
            int fill = block.fill(fluidHolder, FluidAction.EXECUTE);
            itemHandler.drain(fill, FluidAction.EXECUTE);
            return fill > 0;
        }
        return false;
    }

    public boolean fillItemStackFromBlock(BlockEntity be, FluidHandler block, ItemStack stack, int amount, int slot) {
        return fillItemStackFromBlock(block, getFabricHandler(be.getLevel(), be.getBlockPos(), null, slot), amount);
    }

    public boolean fillItemStackFromBlock(FluidHandler block, FluidHandlerItem itemHandler, int amount) {
        if (itemHandler.isFluidValid(block.getFluidHolder())){
            FluidHolder fluidHolder = block.drain(amount, FluidAction.EXECUTE);
            block.fill(fluidHolder, FluidAction.EXECUTE); // FABRIC LACKS SIM FUNCTIONS
            int fill = itemHandler.fill(fluidHolder, FluidAction.EXECUTE);
            block.drain(fill, FluidAction.EXECUTE);
            return fill > 0;
        }
        return false;
    }

    private FluidHandlerItem getFabricHandler(Player player, InteractionHand hand) {
        return new FabricFluidHandler.ItemFluidHandler(ContainerItemContext.ofPlayerHand(player, hand).find(FluidStorage.ITEM), player.getItemInHand(hand));
    }

    private FluidHandlerItem getFabricHandler(Level level, BlockPos pos, Direction direction, int slot) {
        InventoryStorage storage = (InventoryStorage) ItemStorage.SIDED.find(level, pos, direction);
        return new FabricFluidHandler.ItemFluidHandler(ContainerItemContext.ofSingleSlot(storage.getSlot(slot)).find(FluidStorage.ITEM), storage.getSlot(slot).getResource().toStack());
    }
}
