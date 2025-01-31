package mod.syconn.swe.common.items;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import mod.syconn.swe.core.ModFluids;
import mod.syconn.swe.server.reloaders.PlanetManager;
import mod.syconn.swe.util.RenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Canister extends Item implements EquipmentItem, FluidHolderItem {

    public Canister(Rarity rarity) {
        super(new Properties().stacksTo(1).rarity(rarity));
    }

    public boolean isBarVisible(ItemStack stack) {
        if (isEmpty(stack)) return false;
        return getDisplayValue(stack) != 0.6F;
    }

    public static float getDisplayValue(ItemStack stack){
        if (stack.getItem() instanceof FluidHolderItem holder && !holder.isEmpty(stack)) return (float) (holder.getFluidStack(stack).getAmount()) / holder.getMax(stack) * 6.0f / 10f;
        return 0;
    }

    public int getBarColor(ItemStack stack) {
        if (isEmpty(stack)) return -1;
        return RenderUtil.getFluidColor(getFluidStack(stack).getFluid());
    }

    public int getBarWidth(ItemStack stack) {
        return (int) (13 * getFluidStack(stack).getAmount() / getMax(stack));
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(-1,117, 116, 116);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (isEmpty(stack)) {
            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.literal(getFluidStack(stack).getAmount() + "mb / " + getMax(stack) + "mb").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    public void equipmentTick(ItemStack stack, Player player) {
        if (!player.level().isClientSide){
            if (getFluidStack(stack).getFluid().isSame(Fluids.LAVA)) {
                if (!player.fireImmune()) player.setSecondsOnFire(3);
                player.hurt(player.level().damageSources().inFire(), 2f); // TODO CUSTOM DAMAGE TYPE
            } else if (getFluidStack(stack).getFluid().isSame(ModFluids.O2.get()) && !PlanetManager.getSettings(player).breathable() && !player.isCreative()) {
                drain(stack, 1, false);
            }
        }
    }

    public Component getName(ItemStack stack) {
        if (!isEmpty(stack)) return getFluidStack(stack).getName().copy().append(super.getName(stack));
        return Component.literal("Empty ").append(super.getName(stack));
    }

    public int getSlot() {
        return SpaceArmor.TANK;
    }

    public long defaultMax() {
        return FluidStack.bucketAmount() * 16;
    }
}
