package mod.syconn.swe.items;

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
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level l, Player p, InteractionHand hand) {
        if (!l.isClientSide) {
            ItemStack stack = p.getItemInHand(hand);
            if (p.isShiftKeyDown()){
                stack.getOrCreateTag().putBoolean("exporter", !stack.getOrCreateTag().getBoolean("exporter"));
                p.displayClientMessage(Component.literal(stack.getOrCreateTag().getBoolean("exporter") ? "Export Mode" : "Import Mode").withStyle(ChatFormatting.AQUA), true);
            }
        }
        return super.use(l, p, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("Mode: " + (stack.getOrCreateTag().getBoolean("exporter") ? "Export" : "Import")));
        super.appendHoverText(stack, p_41422_, p_41423_, p_41424_);
    }
}
