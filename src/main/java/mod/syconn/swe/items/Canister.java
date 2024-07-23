package mod.syconn.swe.items;

import mod.syconn.swe.Registration;
import mod.syconn.swe.client.RenderUtil;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.world.container.slot.EquipmentItemSlot;
import mod.syconn.swe.world.dimensions.PlanetManager;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

/** USED FOR LIQUIDS ONLY */
public class Canister extends Item implements EquipmentItem { // TODO LOOK AT SHULKER BOX CODE

    public Canister(Rarity rarity) {
        super(new Properties().stacksTo(1).rarity(rarity).component(Registration.FLUID_COMPONENT, SimpleFluidContent.EMPTY));
    }

    public boolean isBarVisible(ItemStack stack) {
        if (getHandler(stack) == null || getHandler(stack).getFluidInTank(0).is(Fluids.EMPTY)) return false;
        return getDisplayValue(stack) != 6F;
    }

    public int getBarColor(ItemStack stack) {
        if (getHandler(stack) == null) return -1;
        return RenderUtil.getFluidColor(getHandler(stack).getFluidInTank(0));
    }

    public int getBarWidth(ItemStack stack) {
        return 13 * getHandler(stack).getFluidInTank(0).getAmount() / getHandler(stack).getTankCapacity(0);
    }

    public static float getDisplayValue(ItemStack stack){
        if (getHandler(stack) != null)
            return (float) (getHandler(stack).getFluidInTank(0).getAmount()) / getHandler(stack).getTankCapacity(0) * 6.0f;
        return 0;
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(117, 116, 116);
    }

    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (getHandler(pStack) != null && !getHandler(pStack).getFluidInTank(0).is(Fluids.EMPTY)) {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.literal(getHandler(pStack).getFluidInTank(0).getAmount() + "mb / " + getHandler(pStack).getTankCapacity(0) + "mb").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide){
            if (getHandler(stack).getFluidInTank(0).is(Fluids.LAVA)) {
                if (!player.fireImmune()) player.igniteForSeconds(3.0F);
                player.hurt(level.damageSources().inFire(), 2f);
            } else if (getHandler(stack).getFluidInTank(0).is(Registration.O2_FLUID_TYPE.get()) && !PlanetManager.getSettings(player).breathable() && !player.isCreative()) {
                getHandler(stack).drain(1, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    public Component getName(ItemStack stack) {
        if (!getHandler(stack).getFluidInTank(0).is(Fluids.EMPTY)) return getHandler(stack).getFluidInTank(0).getHoverName().copy().append(" ").append(super.getName(stack));
        return Component.literal("Empty ").append(super.getName(stack));
    }

    public EquipmentItemSlot.SpaceSlot getSlot() {
        return EquipmentItemSlot.SpaceSlot.TANK;
    }
//
    public static ItemStack create(int volume, int max, Fluid fluid, Item item){
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(Registration.FLUID_COMPONENT, SimpleFluidContent.copyOf(new FluidStack(fluid, volume)));
        return itemStack;
    }

    public ItemStack create(FluidStack stack) {
        return create(stack.getAmount(), 8000, stack.getFluid(), Registration.CANISTER.get());
    }

    public static IFluidHandlerItem getHandler(ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM);
    }
}
