package mod.syconn.swe.items;

import mod.syconn.swe.Registration;
import mod.syconn.swe.client.RenderUtil;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.items.extras.ItemFluidHandler;
import mod.syconn.swe.world.container.slot.EquipmentItemSlot;
import mod.syconn.swe.world.data.components.CanisterComponent;
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
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

/** USED FOR LIQUIDS ONLY */
public class Canister extends Item implements EquipmentItem, ItemFluidHandler {

    public Canister(Rarity rarity) {
        super(new Properties().stacksTo(1).rarity(rarity).component(Registration.CANISTER_COMPONENT, CanisterComponent.DEFAULT));
    }

    public boolean isBarVisible(ItemStack stack) {
        if (get(stack).fluidType().is(Fluids.EMPTY)) return false;
        return getDisplayValue(stack) != 6F;
    }

    public int getBarColor(ItemStack stack) {
        return RenderUtil.getFluidColor(get(stack).fluidStack());
    }

    public int getBarWidth(ItemStack stack) {
        return 13 * get(stack).volume() / get(stack).max();
    }

    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (!get(pStack).fluidType().is(Fluids.EMPTY)) {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.literal(get(pStack).volume() + "mb / " + get(pStack).max() + "mb").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide){
            if (get(stack).fluidType().is(Fluids.LAVA)) {
                if (!player.fireImmune()) player.igniteForSeconds(3.0F);
                player.hurt(level.damageSources().inFire(), 2f);
            }
//           TODO OLD else if (get(stack).fluidType().is(Registration.O2_FLUID_TYPE.get()) && !AirBubblesSavedData.get().breathable(player.level().dimension(), player.getOnPos().above(1)) && !player.isCreative()) {
//                setAmount(stack, get(stack).volume() - 1, getFluid(stack).getFluid());
//            }
            else if (get(stack).fluidType().is(Registration.O2_FLUID_TYPE.get()) && !PlanetManager.getSettings(player).breathable() && !player.isCreative()) {
                setAmount(stack, get(stack).volume() - 1, getFluid(stack).getFluid());
            }
        }
    }

    public Component getName(ItemStack stack) {
        if (!get(stack).fluidType().is(Fluids.EMPTY)) return getFluid(stack).getHoverName().copy().append(" ").append(super.getName(stack));
        return Component.literal("Empty ").append(super.getName(stack));
    }

    public EquipmentItemSlot.SpaceSlot getSlot() {
        return EquipmentItemSlot.SpaceSlot.TANK;
    }

    public static ItemStack create(int volume, int max, Fluid fluid, Item item){
        ItemStack itemStack = new ItemStack(item);
        FluidStack fluidStack = new FluidStack(fluid, 1);
        itemStack.set(Registration.CANISTER_COMPONENT, new CanisterComponent(fluidStack, volume, max));
        return itemStack;
    }

    public void setAmount(ItemStack stack, int v, Fluid fluid) {
        stack.update(Registration.CANISTER_COMPONENT, CanisterComponent.DEFAULT, canister -> canister.set(new FluidStack(fluid, Math.min(v, canister.max()))));
    }

    public void setFluid(ItemStack item, FluidStack fluid) {
        item.update(Registration.CANISTER_COMPONENT, CanisterComponent.DEFAULT, canister -> canister.set(fluid));
    }

    public FluidStack getFluid(ItemStack stack) {
        return get(stack).fluidStack();
    }

    public static CanisterComponent get(ItemStack stack) {
        return stack.get(Registration.CANISTER_COMPONENT);
    }

    public static float getDisplayValue(ItemStack stack){
        if (stack.has(Registration.CANISTER_COMPONENT))
            return (float) (get(stack).volume()) / get(stack).max() * 6.0f;
        return 0;
    }

    public static boolean increaseFluid(ItemStack pStack, FluidStack fluid) {
        if (get(pStack).fluidType().is(fluid.getFluidType())) {
            pStack.update(Registration.CANISTER_COMPONENT, CanisterComponent.DEFAULT, canister -> canister.increase(fluid));
            return true;
        }
        else if (get(pStack).fluidType() == FluidStack.EMPTY) {
            pStack.update(Registration.CANISTER_COMPONENT, CanisterComponent.DEFAULT, canister -> canister.set(fluid));
            return true;
        }
        return false;
    }

    public static void clone(ItemStack stack, ItemStack item){
        stack.set(Registration.CANISTER_COMPONENT, item.get(Registration.CANISTER_COMPONENT));
    }

    public ItemStack create(FluidStack stack) {
        return create(stack.getAmount(), 8000, stack.getFluid(), Registration.CANISTER.get());
    }

    public ItemStack createEmpty() {
        return create(0, 8000, Fluids.EMPTY, Registration.CANISTER.get());
    }

    public int getColor(ItemStack stack) {
        return getBarColor(stack);
    }

    public int getCapacity(ItemStack stack) {
        return get(stack).max();
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(117, 116, 116);
    }
}
