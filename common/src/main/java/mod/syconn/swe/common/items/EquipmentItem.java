package mod.syconn.swe.common.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface EquipmentItem {

    int getSlot();

    void equipmentTick(ItemStack stack, Player player);
}
