package mod.syconn.swe.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class UpgradeItem extends Item {

    private final int upgradeSpeed;

    public UpgradeItem(Properties p_41383_, int speed) {
        super(p_41383_.rarity(Rarity.EPIC));
        upgradeSpeed = speed;
    }

    public int getUpgradeSpeed() {
        return upgradeSpeed;
    }

    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("Upgrade Speed: " + upgradeSpeed));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
