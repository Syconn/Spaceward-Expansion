package mod.syconn.swe.common.items;

import dev.architectury.fluid.FluidStack;
import mod.syconn.swe.core.ModFluids;
import mod.syconn.swe.server.reloaders.PlanetManager;
import mod.syconn.swe.util.RenderUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

import static mod.syconn.swe.common.items.FluidHolderItem.IFluidHolderItem;

public class Canister extends Item implements EquipmentItem, IFluidHolderItem {

    public Canister(Rarity rarity) {
        super(new Properties().stacksTo(1).rarity(rarity));
    }

    public boolean isBarVisible(ItemStack stack) {
        FluidHolderItem fluidHolder = getOnClient(stack);
        if (fluidHolder != null && fluidHolder.isEmpty()) return false;
        return getDisplayValue(stack) != 0.6F;
    }

    public int getBarColor(ItemStack stack) {
        FluidHolderItem fluidHolder = getOnClient(stack);
        if (fluidHolder != null) return RenderUtil.getFluidColor(fluidHolder.getFluidStack());
        return super.getBarColor(stack);
    }

    public int getBarWidth(ItemStack stack) {
        FluidHolderItem fluidHolder = getOnClient(stack);
        if (fluidHolder != null) return (int) (13 * fluidHolder.getFluidStack().getAmount() / getCapacity());
        return super.getBarWidth(stack);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        FluidHolderItem fluidHolder = getOnClient(stack);
        if (fluidHolder != null && fluidHolder.isEmpty()) {
            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.literal(fluidHolder.getFluidStack().getAmount() + "mb / " + getCapacity() + "mb").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    public void equipmentTick(ItemStack stack, Player player) {
        FluidHolderItem fluidHolder = getFluidHolder(player, player.containerMenu, stack);
        if (!player.level().isClientSide){
            if (fluidHolder.getFluidStack().getFluid().isSame(Fluids.LAVA)) {
                if (!player.fireImmune()) player.setSecondsOnFire(3);
                player.hurt(player.level().damageSources().inFire(), 2f); // TODO CUSTOM DAMAGE TYPE
            } else if (fluidHolder.getFluidStack().getFluid().isSame(ModFluids.O2.get()) && !PlanetManager.getSettings(player).breathable() && !player.isCreative()) {
                fluidHolder.pull(1, false);
            }
        }
    }

    public Component getName(ItemStack stack) {
        FluidHolderItem fluidHolder = getOnClient(stack);
        if (fluidHolder != null && !fluidHolder.isEmpty()) return fluidHolder.getFluidStack().getName().copy().append(super.getName(stack));
        return Component.literal("Empty ").append(super.getName(stack));
    }

    public int getSlot() {
        return SpaceArmor.TANK;
    }

    public long getCapacity() {
        return FluidStack.bucketAmount() * 16;
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(-1,117, 116, 116);
    }

    @Environment(EnvType.CLIENT)
    public static float getDisplayValue(ItemStack stack) {
        if (stack.getItem() instanceof IFluidHolderItem holder) {
            FluidHolderItem fluidHolder = holder.getOnClient(stack);
            if (fluidHolder != null && fluidHolder.isEmpty()) return (float) (fluidHolder.getFluidStack().getAmount()) / holder.getCapacity() * 6.0f / 10f;
        }
        return 0;
    }
}
