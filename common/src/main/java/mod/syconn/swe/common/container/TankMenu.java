package mod.syconn.swe.common.container;

import mod.syconn.swe.blockentities.TankBE;
import mod.syconn.swe.common.container.slot.SpecifiedSlotHandler;
import mod.syconn.swe.extra.data.menu.PositionMenuData;
import mod.syconn.swe.init.BlockEntityRegister;
import mod.syconn.swe.init.CommonTags;
import mod.syconn.swe.init.Menus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TankMenu extends AbstractContainerMenu {

    private final TankBE be;

    public TankMenu(int id, Inventory inventory, PositionMenuData data) {
        super(Menus.TANK_MENU.get(), id);
        this.be = inventory.player.level().getBlockEntity(data.pos(), BlockEntityRegister.TANK.get()).orElseThrow();
        this.addSlot(new Slot(be, 0, 14, 9));
        this.addSlot(new Slot(be, 1, 14, 61));
        this.addSlot(new SpecifiedSlotHandler(be, 2, 72, 9, CommonTags.CANISTERS));

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
        }
    }

    public TankBE getBE() {
        return be;
    }

    public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);
        if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();
            if (quickMovedSlotIndex >= 5 && quickMovedSlotIndex < 41) {
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
