package mod.syconn.swe.items;

import mod.syconn.swe.init.ItemRegister;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;

import java.util.EnumMap;
import java.util.Map;

public class SpaceArmor extends ArmorItem {

    public static final Map<Type, Integer> DEFENSE = Util.make(new EnumMap<>(Type.class), map -> {
        map.put(Type.BOOTS, 2);
        map.put(Type.LEGGINGS, 5);
        map.put(Type.CHESTPLATE, 6);
        map.put(Type.HELMET, 2);
        map.put(Type.BODY, 4);
    });

    public SpaceArmor(Type pType) {
        super(ItemRegister.SPACE_SUIT_MATERIAL, pType, new Properties().durability(200));
    }

    public static boolean hasFullKit(Player p){
        return p.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem
                && p.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem;
    }

//    public static boolean hasParachute(Player p){ TODO LATER
//        return p.getInventory() instanceof ExtendedPlayerInventory i && i.getItemBySlot(EquipmentItemSlot.SpaceSlot.PARACHUTE).getItem() instanceof Parachute && hasFullKit(p);
//    }
//
//    public static ItemStack getGear(EquipmentItemSlot.SpaceSlot slot, Player p){
//        if (p.getInventory() instanceof ExtendedPlayerInventory i){
//            return i.getItemBySlot(slot);
//        } return null;
//    }
}
