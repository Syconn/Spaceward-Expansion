package mod.syconn.swe.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import mod.syconn.swe.common.inventory.ExtendedPlayerInventory;
import mod.syconn.swe.init.ModArmorTypes;
import mod.syconn.swe.util.data.SpaceSlot;

public class SpaceArmor extends ArmorItem {
    public SpaceArmor(Type p_266831_, Properties p_40388_) {
        super(ModArmorTypes.SPACE_SUIT, p_266831_, p_40388_);
    }

    public static boolean hasFullKit(Player p){
        return p.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem
                && p.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem && p.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorItem;
    }

    public static boolean hasParachute(Player p){
        return p.getInventory() instanceof ExtendedPlayerInventory i && i.getItemBySlot(SpaceSlot.PARACHUTE).getItem() instanceof Parachute && hasFullKit(p);
    }

    public static @Nullable ItemStack getGear(SpaceSlot slot, Player p){
        if (p.getInventory() instanceof ExtendedPlayerInventory i){
            return i.getItemBySlot(slot);
        } return null;
    }
}
