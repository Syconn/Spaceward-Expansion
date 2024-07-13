package mod.syconn.swe.world.container.slot;

import com.mojang.datafixers.util.Pair;
import mod.syconn.swe.Main;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.items.extras.EquipmentItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class EquipmentItemSlot extends SlotItemHandler {

    private final Player player;
    private final SpaceSlot slot;

    public EquipmentItemSlot(Player p, SpaceSlot s, IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        player = p;
        slot = s;
    }

    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, Main.loc("custom/" + slot.getLoc()));
    }

    public boolean isActive() {
        return SpaceArmor.hasFullKit(player) && !player.mayFly();
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof EquipmentItem && isActive() && ((EquipmentItem) stack.getItem()).getSlot() == slot;
    }

    public enum SpaceSlot {

        TANK("empty_canister"),
        PARACHUTE("empty_parachute");

        final String loc;

        SpaceSlot(String loc) {
            this.loc = loc;
        }

        public String getLoc() {
            return loc;
        }
    }
}
