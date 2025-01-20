package mod.syconn.swe.server.container;

import mod.syconn.swe.server.container.slot.EquipmentItemSlot;
import mod.syconn.swe.extra.data.attachment.SpaceSuit;
import mod.syconn.swe.extra.platform.Services;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;

public class ExtendedPlayerContainer extends InventoryMenu {

    public ExtendedPlayerContainer(Inventory playerInventory, boolean localWorld, Player playerIn) {
        super(playerInventory, localWorld, playerIn);
        SpaceSuit suit = Services.ATTACHED_DATA.get(DataAttachments.SPACE_SUIT, playerIn);
        this.addSlot(new EquipmentItemSlot(playerIn, EquipmentItemSlot.SpaceSlot.TANK, suit, 0, 77, 44));
        this.addSlot(new EquipmentItemSlot(playerIn, EquipmentItemSlot.SpaceSlot.PARACHUTE, suit, 1, 77, 26));
    }
}
