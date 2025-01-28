package mod.syconn.swe.server.container.slot;

import com.mojang.datafixers.util.Pair;
import mod.syconn.swe.Constants;
import mod.syconn.swe.extra.EquipmentItem;
import mod.syconn.swe.items.SpaceArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EquipmentItemSlot extends Slot {

    private final Player player;
    private final SpaceSlot slot;

    public EquipmentItemSlot(Player p, SpaceSlot s, Container container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
        player = p;
        slot = s;
    }

    public void setChanged() {
        player.getInventory().setChanged();
        super.setChanged();
    }

    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, Constants.loc("custom/" + slot.getLoc()));
    }

    public boolean isActive() {
        return SpaceArmor.hasFullKit(player) && !player.isCreative();
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof EquipmentItem && isActive() && ((EquipmentItem) stack.getItem()).getSlot() == slot;
    }
}
