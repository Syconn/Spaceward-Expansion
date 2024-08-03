package mod.syconn.swe2.items;

import mod.syconn.swe2.Registration;
import mod.syconn.swe2.world.container.slot.EquipmentItemSlot;
import mod.syconn.swe2.world.inventory.ExtendedPlayerInventory;
import net.minecraft.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.Map;

public class SpaceArmor extends ArmorItem {

    public static final Map<Type, Integer> DEFENSE = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 2);
        map.put(ArmorItem.Type.LEGGINGS, 5);
        map.put(ArmorItem.Type.CHESTPLATE, 6);
        map.put(ArmorItem.Type.HELMET, 2);
        map.put(ArmorItem.Type.BODY, 4);
    });

    public SpaceArmor(Type pType) {
        super(Registration.SPACE_SUIT_MATERIAL, pType, new Item.Properties().durability(200));
    }

    public static boolean hasFullKit(Player p){
        return p.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem
                && p.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem;
    }

    public static boolean hasParachute(Player p){
        return p.getInventory() instanceof ExtendedPlayerInventory i && i.getItemBySlot(EquipmentItemSlot.SpaceSlot.PARACHUTE).getItem() instanceof Parachute && hasFullKit(p);
    }

    public static ItemStack getGear(EquipmentItemSlot.SpaceSlot slot, Player p){
        if (p.getInventory() instanceof ExtendedPlayerInventory i){
            return i.getItemBySlot(slot);
        } return null;
    }
}
