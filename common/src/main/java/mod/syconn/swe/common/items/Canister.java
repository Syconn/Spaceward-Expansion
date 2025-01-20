package mod.syconn.swe.common.items;

import mod.syconn.swe.common.container.slot.EquipmentItemSlot;
import mod.syconn.swe.server.reloaders.PlanetManager;
import mod.syconn.swe.extra.EquipmentItem;
import mod.syconn.swe.extra.core.FluidAction;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.data.components.FluidHolderComponent;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.extra.util.RenderUtil;
import mod.syconn.swe.core.FluidRegister;
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
        super(new Properties().stacksTo(1).rarity(rarity).component(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.EMPTY));
    }

    public boolean isBarVisible(ItemStack stack) {
        if (getHandler(stack) == null || getHandler(stack).getFluidHolder().is(Fluids.EMPTY)) return false;
        return getDisplayValue(stack) != 0.6F;
    }

    public int getBarColor(ItemStack stack) {
        if (getHandler(stack) == null) return -1;
        return RenderUtil.getFluidColor(getHandler(stack).getFluidHolder().getFluid());
    }

    public int getBarWidth(ItemStack stack) {
        return 13 * getHandler(stack).getFluidHolder().getAmount() / getHandler(stack).getTankCapacity();
    }

    public static float getDisplayValue(ItemStack stack){
        if (getHandler(stack) != null) return (float) (getHandler(stack).getFluidHolder().getAmount()) / getHandler(stack).getTankCapacity() * 6.0f / 10f;
        return 0;
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(117, 116, 116);
    }

    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (getHandler(pStack) != null && !getHandler(pStack).getFluidHolder().is(Fluids.EMPTY)) {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.literal(getHandler(pStack).getFluidHolder().getAmount() + "mb / " + getHandler(pStack).getTankCapacity() + "mb").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide){
            if (getHandler(stack).getFluidHolder().is(Fluids.LAVA)) {
                if (!player.fireImmune()) player.igniteForSeconds(3.0F);
                player.hurt(level.damageSources().inFire(), 2f);
            } else if (getHandler(stack).getFluidHolder().is(FluidRegister.O2.get()) && !PlanetManager.getSettings(player).breathable() && !player.isCreative()) {
                getHandler(stack).drain(1, FluidAction.EXECUTE);
            }
        }
    }

    public Component getName(ItemStack stack) {
        if (!getHandler(stack).getFluidHolder().is(Fluids.EMPTY)) return Services.FLUID_EXTENSIONS.getTooltip(getHandler(stack).getFluidHolder().getFluid()).getFirst().copy().append(" ").append(super.getName(stack));
        return Component.literal("Empty ").append(super.getName(stack));
    }

    public EquipmentItemSlot.SpaceSlot getSlot() {
        return EquipmentItemSlot.SpaceSlot.TANK;
    }

    public static ItemStack create(int volume, int max, Fluid fluid, Item item) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.of(fluid, volume, max));
        return itemStack;
    }

    public static ItemStack createEmpty(Item item) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), FluidHolderComponent.of(Fluids.EMPTY, 0, 800));
        return itemStack;
    }

    public static FluidHandlerItem getHandler(ItemStack stack) {
        if (!Services.FLUID_HANDLER.has(stack)) return null;
        return Services.FLUID_HANDLER.get(stack);
    }
}
