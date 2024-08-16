package mod.syconn.swe.items;

import mod.syconn.swe.common.container.slot.EquipmentItemSlot;
import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.extra.EquipmentItem;
import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.data.components.FluidComponent;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.extra.util.RenderUtil;
import mod.syconn.swe.init.ComponentRegister;
import mod.syconn.swe.init.FluidRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

/** USED FOR LIQUIDS ONLY */
public class Canister extends Item implements EquipmentItem {

    public Canister(Rarity rarity) {
        super(new Properties().stacksTo(1).rarity(rarity).component(ComponentRegister.FLUID_COMPONENT.get(), FluidComponent.EMPTY));
    }

    public boolean isBarVisible(ItemStack stack) {
        if (getHandler(stack) == null || getHandler(stack).getFluid().is(Fluids.EMPTY)) return false;
        return getDisplayValue(stack) != 6F;
    }

    public int getBarColor(ItemStack stack) {
        if (getHandler(stack) == null) return -1;
        return RenderUtil.getFluidColor(getHandler(stack).getFluid().getFluid());
    }

    public int getBarWidth(ItemStack stack) {
        return 13 * getHandler(stack).getFluid().getAmount() / getHandler(stack).getCapacity();
    }

    public static float getDisplayValue(ItemStack stack){
        if (getHandler(stack) != null)
            return (float) (getHandler(stack).getFluid().getAmount()) / getHandler(stack).getCapacity() * 6.0f;
        return 0;
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(117, 116, 116);
    }

    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (getHandler(pStack) != null && !getHandler(pStack).getFluid().is(Fluids.EMPTY)) {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.literal(getHandler(pStack).getFluid().getAmount() + "mb / " + getHandler(pStack).getCapacity() + "mb").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide){
            if (getHandler(stack).getFluid().is(Fluids.LAVA)) {
                if (!player.fireImmune()) player.igniteForSeconds(3.0F);
                player.hurt(level.damageSources().inFire(), 2f);
            } else if (getHandler(stack).getFluid().is(FluidRegister.O2.get()) && !PlanetManager.getSettings(player).breathable() && !player.isCreative()) {
                getHandler(stack).drain(1, FluidAction.EXECUTE);
            }
        }
    }

    public Component getName(ItemStack stack) {
        if (!getHandler(stack).getFluid().is(Fluids.EMPTY)) return Services.FLUID_EXTENSIONS.getTooltip(getHandler(stack).getFluid().getFluid()).getFirst().copy().append(" " + super.getName(stack));
        return Component.literal("Empty ").append(super.getName(stack));
    }

    public EquipmentItemSlot.SpaceSlot getSlot() {
        return EquipmentItemSlot.SpaceSlot.TANK;
    }

    public static ItemStack create(int volume, int max, Fluid fluid, Item item) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(ComponentRegister.FLUID_COMPONENT.get(), FluidComponent.of(fluid, volume, max));
        return itemStack;
    }

    public static ItemStack createEmpty(Item item) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(ComponentRegister.FLUID_COMPONENT.get(), FluidComponent.of(Fluids.EMPTY, 0, 800));
        return itemStack;
    }

    public static FluidHandlerItem getHandler(ItemStack stack) {
        return Services.FLUID_HANDLER.get(stack);
    }
}
