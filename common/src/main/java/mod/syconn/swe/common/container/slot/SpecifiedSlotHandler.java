package mod.syconn.swe.common.container.slot;

import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpecifiedSlotHandler extends Slot {

    private final TagKey<Item> onlyType;

    public SpecifiedSlotHandler(Container container, int index, int xPosition, int yPosition, TagKey<Item> item) {
        super(container, index, xPosition, yPosition);
        this.onlyType = item;
    }

    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(onlyType);
    }
}
