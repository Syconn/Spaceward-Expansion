package mod.syconn.swe.world.container;

import mod.syconn.swe.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import mod.syconn.swe.blockentities.PipeBlockEntity;
import mod.syconn.swe.world.container.slot.ToggledHandlerSlot;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class PipeMenu extends AbstractContainerMenu {

    private final PipeBlockEntity be;

    public PipeMenu(int id, Inventory inventory, BlockPos pos) {
        super(Registration.PIPE_MENU.get(), id);
        this.be = inventory.player.level().getBlockEntity(pos, Registration.PIPE.get()).orElseThrow();
        IItemHandler handler = inventory.player.level().getCapability(Capabilities.ItemHandler.BLOCK, pos, Direction.NORTH);
        if (handler != null){
            addSlot(new ToggledHandlerSlot(handler, 0, 152, 13, be));
            addSlot(new ToggledHandlerSlot(handler, 1, 152, 35, be));
            addSlot(new ToggledHandlerSlot(handler, 2, 152, 57, be));
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
        }
    }

    public PipeBlockEntity getBE() {
        return be;
    }

    public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);
        if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();
            if (quickMovedSlotIndex == 0) {
//                if (!this.moveItemStackTo(rawStack, 5, 41, true)) {
//                    return ItemStack.EMPTY;
//                }
            }
            else if (quickMovedSlotIndex >= 5 && quickMovedSlotIndex < 41) {
                if (!this.moveItemStackTo(rawStack, 1, 5, false)) {
                    if (quickMovedSlotIndex < 32) {
                        if (!this.moveItemStackTo(rawStack, 32, 41, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(rawStack, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
//            else if (!this.moveItemStackTo(rawStack, 5, 41, false)) {
//                return ItemStack.EMPTY;
//            }

            if (rawStack.isEmpty()) {
                quickMovedSlot.set(ItemStack.EMPTY);
            } else {
                quickMovedSlot.setChanged();
            }

            if (rawStack.getCount() == quickMovedStack.getCount()) {
                return ItemStack.EMPTY;
            }
            quickMovedSlot.onTake(player, rawStack);
        }

        return quickMovedStack;
    }

    public boolean stillValid(Player p_38874_) {
        return true;
    }
}
