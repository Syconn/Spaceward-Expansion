package mod.syconn.swe2.items.extras;

import mod.syconn.swe2.world.container.slot.EquipmentItemSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface EquipmentItem {

    EquipmentItemSlot.SpaceSlot getSlot();

    void onEquipmentTick(ItemStack stack, Level level, Player player);
}
