package mod.syconn.swe.common.items;

import mod.syconn.swe.registry.ModArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;

public class SpaceArmor extends ArmorItem {

    public static final int PARACHUTE = 0;
    public static final int TANK = 1;

    public SpaceArmor(Type pType) {
        super(ModArmor.SPACESUIT, pType, new Properties().durability(200));
    }

    public static boolean wearingSpaceSuit(Player p){
        return p.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem
                && p.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem;
    }
}
