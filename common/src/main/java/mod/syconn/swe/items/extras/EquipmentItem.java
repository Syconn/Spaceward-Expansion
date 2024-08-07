package mod.syconn.swe.items.extras;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface EquipmentItem {

//    EquipmentItemSlot.SpaceSlot getSlot(); TODO RE ADD

    void onEquipmentTick(ItemStack stack, Level level, Player player);
}
