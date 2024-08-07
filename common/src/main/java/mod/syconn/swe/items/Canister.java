package mod.syconn.swe.items;

import mod.syconn.swe.api.client.RenderUtil;
import mod.syconn.swe.data.components.FluidComponent;
import mod.syconn.swe.init.ComponentRegister;
import mod.syconn.swe.platform.Services;
import mod.syconn.swe.platform.services.ISingleFluidHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

/** USED FOR LIQUIDS ONLY */
public class Canister extends Item
//        implements EquipmentItem TODO LATER
{

    public Canister(Rarity rarity) {
        super(new Properties().stacksTo(1).rarity(rarity).component(ComponentRegister.FLUID_COMPONENT.get(), FluidComponent.EMPTY));
    }

    public boolean isBarVisible(ItemStack stack) {
        if (getHandler(stack) == null || getHandler(stack).getFluidInTank().is(Fluids.EMPTY)) return false;
        return getDisplayValue(stack) != 6F;
    }

    public int getBarColor(ItemStack stack) {
        if (getHandler(stack) == null) return -1;
        return RenderUtil.getFluidColor(getHandler(stack).getFluidInTank().fluid());
    }

    public int getBarWidth(ItemStack stack) {
        return 13 * getHandler(stack).getFluidInTank().amount() / getHandler(stack).getTankCapacity();
    }

    public static float getDisplayValue(ItemStack stack){
        if (getHandler(stack) != null)
            return (float) (getHandler(stack).getFluidInTank().amount()) / getHandler(stack).getTankCapacity() * 6.0f;
        return 0;
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(117, 116, 116);
    }

    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (getHandler(pStack) != null && !getHandler(pStack).getFluidInTank().is(Fluids.EMPTY)) {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.literal(getHandler(pStack).getFluidInTank().amount() + "mb / " + getHandler(pStack).getTankCapacity() + "mb").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
//
//    public void onEquipmentTick(ItemStack stack, Level level, Player player) { TODO REDO
//        if (!level.isClientSide){
//            if (getHandler(stack).getFluidInTank(0).is(Fluids.LAVA)) {
//                if (!player.fireImmune()) player.igniteForSeconds(3.0F);
//                player.hurt(level.damageSources().inFire(), 2f);
//            } else if (getHandler(stack).getFluidInTank(0).is(Registration.O2_FLUID_TYPE.get()) && !PlanetManager.getSettings(player).breathable() && !player.isCreative()) {
//                getHandler(stack).drain(1, IFluidHandler.FluidAction.EXECUTE);
//            }
//        }
//    }

    public Component getName(ItemStack stack) {
        if (!getHandler(stack).getFluidInTank().is(Fluids.EMPTY)) return Services.FLUID_EXTENSIONS.getTooltip(getHandler(stack).getFluidInTank().fluid()).get(0).copy().append(super.getName(stack));
        return Component.literal("Empty ").append(super.getName(stack));
    }

//    public EquipmentItemSlot.SpaceSlot getSlot() {
//        return EquipmentItemSlot.SpaceSlot.TANK;TODO LATER
//    }

    public static ItemStack create(int volume, int max, Fluid fluid, Item item){
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(ComponentRegister.FLUID_COMPONENT.get(), FluidComponent.of(fluid, volume, max));
        return itemStack;
    }

    public static ISingleFluidHandler getHandler(ItemStack stack) {
        return Services.FLUID_HANDLER.get(stack);
    }
}
