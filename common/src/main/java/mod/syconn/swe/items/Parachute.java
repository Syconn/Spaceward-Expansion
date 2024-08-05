package mod.syconn.swe.items;

import mod.syconn.swe.items.extras.EquipmentItem;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.Optional;

public class Parachute extends Item implements Equipable, EquipmentItem {

    public Parachute() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public InteractionResultHolder<ItemStack> use(Level l, Player p, InteractionHand h) {
        return this.swapWithEquipmentSlot(this, l, p, h);
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack p_150902_) {
        return super.getTooltipImage(p_150902_);
    }

    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    public Holder<SoundEvent> getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_ELYTRA;
    }

//    public EquipmentItemSlot.SpaceSlot getSlot() { TODO RE ADD
//        return EquipmentItemSlot.SpaceSlot.PARACHUTE;
//    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) { }
}
