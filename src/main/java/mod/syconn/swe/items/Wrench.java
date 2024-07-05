package mod.syconn.swe.items;

import mod.syconn.swe.Registration;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Wrench extends Item {

    public Wrench() {
        super(new Properties().stacksTo(1).component(Registration.MODE_COMPONENT, false));
    }

    public InteractionResultHolder<ItemStack> use(Level l, Player p, InteractionHand hand) {
        if (!l.isClientSide) {
            ItemStack stack = p.getItemInHand(hand);
            if (p.isShiftKeyDown()) {
                stack.set(Registration.MODE_COMPONENT, Boolean.FALSE.equals(stack.get(Registration.MODE_COMPONENT)));
                p.displayClientMessage(Component.literal(Boolean.TRUE.equals(stack.get(Registration.MODE_COMPONENT)) ? "Export Mode" : "Import Mode").withStyle(ChatFormatting.AQUA), true);
            }
        }
        return super.use(l, p, hand);
    }

    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.literal("Mode: " + (Boolean.TRUE.equals(pStack.get(Registration.MODE_COMPONENT)) ? "Export" : "Import")));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
