package mod.syconn.swe.server.container.slot;

import mod.syconn.swe.common.items.EquipmentItem;
import mod.syconn.swe.common.items.SpaceArmor;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EquipmentItemSlot extends Slot {

    private final Player player;
    private final int slot;

    public EquipmentItemSlot(Player p, int s, Container container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
        player = p;
        slot = s;
    }

//    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() { TODO
//        return Pair.of(InventoryMenu.BLOCK_ATLAS, Constants.withId("custom/" + slot.getLoc()));
//    }

    public boolean isActive() {
        return SpaceArmor.wearingSpaceSuit(player) && !player.isCreative();
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof EquipmentItem && isActive() && ((EquipmentItem) stack.getItem()).getSlot() == slot;
    }
}
