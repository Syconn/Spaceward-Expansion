package mod.syconn.swe.world.container.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import mod.syconn.swe.blockentities.PipeBlockEntity;
import mod.syconn.swe.items.UpgradeItem;

public class ToggledHandlerSlot extends SlotItemHandler {

    private final PipeBlockEntity be;

    public ToggledHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, PipeBlockEntity be) {
        super(itemHandler, index, xPosition, yPosition);
        this.be = be;
    }

    @Override
    public boolean isActive() {
        return be.selectedTab() != null;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return isActive() && stack.getItem() instanceof UpgradeItem;
    }
}
