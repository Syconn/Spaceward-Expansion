package mod.syconn.swe.world.container.slot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import mod.syconn.swe.Main;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.util.data.SpaceSlot;

import javax.annotation.Nullable;

public class EquipmentItemSlot extends SlotItemHandler {

    private final Player player;
    private final SpaceSlot slot;

    public EquipmentItemSlot(Player p, SpaceSlot s, IItemHandler inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
        player = p;
        slot = s;
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon()
    {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Main.MODID, "custom/" + slot.getLoc()));
    }

    @Override
    public boolean isActive() {
        return SpaceArmor.hasFullKit(player) && !player.getAbilities().mayfly;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return stack.getItem() instanceof EquipmentItem && isActive() && ((EquipmentItem) stack.getItem()).getSlot() == slot;
    }
}
