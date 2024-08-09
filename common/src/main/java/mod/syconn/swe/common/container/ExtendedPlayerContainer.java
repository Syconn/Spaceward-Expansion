package mod.syconn.swe.common.container;

import mod.syconn.swe.Registration;
import mod.syconn.swe.common.container.slot.EquipmentItemSlot;
import mod.syconn.swe.common.data.attachments.SpaceSuit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;

public class ExtendedPlayerContainer extends InventoryMenu {

    public ExtendedPlayerContainer(Inventory playerInventory, boolean localWorld, Player playerIn) {
        super(playerInventory, localWorld, playerIn);
        SpaceSuit suit = playerIn.getData(Registration.SPACE_SUIT);
        this.addSlot(new EquipmentItemSlot(playerIn, EquipmentItemSlot.SpaceSlot.TANK, suit, 0, 77, 44));
        this.addSlot(new EquipmentItemSlot(playerIn, EquipmentItemSlot.SpaceSlot.PARACHUTE, suit, 1, 77, 26));
    }
}
