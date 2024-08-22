package mod.syconn.swe.extra.platform.services;

import mod.syconn.swe.extra.core.FluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;

public interface IFluidHelper {

    void handleInventoryMaxTransfer(FluidHandler blockHandler, ItemStack stack, Container inventory, int slot1, int slot2, BlockEntity be);
    boolean interactWithBlock(Level level, BlockPos pos, BlockHitResult hitResult, Player player, InteractionHand hand);
    boolean fillItemStackFromBlock(BlockEntity be, FluidHandler block, ItemStack stack, int amount, int slot);
}
