package mod.syconn.swe2.api.containers.slots;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SpecifiedSlotHandler extends SlotItemHandler {

    private final TagKey<Item> onlyType;

    public SpecifiedSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, TagKey<Item> item) {
        super(itemHandler, index, xPosition, yPosition);
        this.onlyType = item;
    }

    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(onlyType);
    }
}
