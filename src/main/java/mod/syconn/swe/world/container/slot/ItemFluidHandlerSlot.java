package mod.syconn.swe.world.container.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import mod.syconn.swe.items.extras.ItemFluidHandler;

public class ItemFluidHandlerSlot extends SlotItemHandler {

    public ItemFluidHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof ItemFluidHandler;
    }
}
