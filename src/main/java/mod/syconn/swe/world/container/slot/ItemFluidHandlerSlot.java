package mod.syconn.swe.world.container.slot;

import com.mojang.datafixers.util.Pair;
import mod.syconn.swe.Main;
import mod.syconn.swe.util.data.SpaceSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import mod.syconn.swe.items.extras.ItemFluidHandler;

public class ItemFluidHandlerSlot extends SlotItemHandler {

    public ItemFluidHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, Main.loc("custom/" + SpaceSlot.TANK.getLoc()));
    }

    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof ItemFluidHandler;
    }
}
