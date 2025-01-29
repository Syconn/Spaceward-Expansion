package mod.syconn.swe.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeItem extends Item {

    private final int upgradeSpeed;

    public UpgradeItem(Properties properties, int speed) {
        super(properties.rarity(Rarity.EPIC));
        upgradeSpeed = speed;
    }

    public int getUpgradeSpeed() {
        return upgradeSpeed;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("Upgrade Speed: " + upgradeSpeed));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
