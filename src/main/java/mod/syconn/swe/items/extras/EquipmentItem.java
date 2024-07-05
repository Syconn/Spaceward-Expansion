package mod.syconn.swe.items.extras;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import mod.syconn.swe.util.data.SpaceSlot;

public interface EquipmentItem {

    SpaceSlot getSlot();

    public void onEquipmentTick(ItemStack stack, Level level, Player player);
}
