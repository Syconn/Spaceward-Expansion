package mod.syconn.swe.world.container;

import mod.syconn.swe.Registration;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.util.data.SpaceSlot;
import mod.syconn.swe.world.container.slot.EquipmentItemSlot;
import mod.syconn.swe.world.data.attachments.SpaceSuit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ExtendedPlayerContainer extends InventoryMenu {

    public ExtendedPlayerContainer(Inventory playerInventory, boolean localWorld, Player playerIn) {
        super(playerInventory, localWorld, playerIn);
        SpaceSuit suit = playerIn.getData(Registration.SPACE_SUIT);
        this.addSlot(new EquipmentItemSlot(playerIn, SpaceSlot.TANK, suit, 0, 77, 44));
        this.addSlot(new EquipmentItemSlot(playerIn, SpaceSlot.PARACHUTE, suit, 1, 77, 26));
    }
}
