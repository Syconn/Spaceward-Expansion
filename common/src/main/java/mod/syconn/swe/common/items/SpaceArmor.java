package mod.syconn.swe.common.items;

import mod.syconn.swe.common.container.slot.EquipmentItemSlot;
import mod.syconn.swe.common.inventory.ExtendedPlayerInventory;
import mod.syconn.swe.core.ModArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class SpaceArmor extends ArmorItem {

    public SpaceArmor(Type pType) {
        super(ModArmor.SPACESUIT, pType, new Properties().durability(200));
    }

    public static boolean hasFullKit(Player p){
        return p.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem
                && p.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem;
    }

    public static boolean hasParachute(Player p) {
        return p.getInventory() instanceof ExtendedPlayerInventory i && i.getItemBySlot(EquipmentItemSlot.SpaceSlot.PARACHUTE).getItem() instanceof Parachute && hasFullKit(p);
    }

    public static ItemStack getGear(EquipmentItemSlot.SpaceSlot slot, Player p){
        if (p.getInventory() instanceof ExtendedPlayerInventory i){
            return i.getItemBySlot(slot);
        } return null;
    }
}
