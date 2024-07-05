package mod.syconn.swe.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import mod.syconn.swe.items.extras.EquipmentItem;
import mod.syconn.swe.items.extras.ItemFluidHandler;
import mod.syconn.swe.util.ResourceUtil;
import mod.syconn.swe.util.data.AirBubblesSavedData;
import mod.syconn.swe.util.data.SpaceSlot;

import java.util.List;
import java.util.Objects;

import static net.minecraft.world.level.material.Fluids.EMPTY;

/** USED FOR LIQUIDS ONLY */
public class Canister extends Item implements EquipmentItem, ItemFluidHandler {

    protected static final String FLUID = "fluid";
    protected static final String MAX = "max";
    protected static final String CURRENT = "current";
    protected static final String COLOR = "color";

    public Canister(Rarity rarity) {
        super(new Properties().stacksTo(1).rarity(rarity));
    }

    public boolean isBarVisible(ItemStack stack) {
        if (getType(stack) == EMPTY) return false;
        return getDisplayValue(stack) != 6F;
    }

    public int getBarColor(ItemStack stack) {
        return stack.getOrCreateTag().getInt(COLOR);
    }

    public int getBarWidth(ItemStack stack) {
        return 13 * getValue(stack) / getMaxValue(stack);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if (getType(stack) != EMPTY) {
            list.add(Component.empty());
            list.add(Component.literal(getValue(stack) + "mb / " + getMaxValue(stack) + "mb").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(stack, level, list, flag);
    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide){
            if (getType(stack) == Fluids.LAVA) {
                player.setSecondsOnFire(2);
            }
            else if (getType(stack).getFluidType() == ModFluids.O2_FLUID_TYPE.get() && !AirBubblesSavedData.get().breathable(player.level.dimension(), player.getOnPos().above(1)) && !player.isCreative()) {
                setAmount(stack, getValue(stack) - 1, getFluid(stack).getFluid());
            }
        }
    }

    public Component getName(ItemStack stack) {
        if (getType(stack) != EMPTY) {
            return getFluid(stack).getDisplayName().copy().append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }

    public SpaceSlot getSlot() {
        return SpaceSlot.TANK;
    }

    public static ItemStack create(int c, int m, Fluid type, Item item){
        ItemStack stack = new ItemStack(item);
        if (c == 0) type = EMPTY;
        stack.getOrCreateTag().putString(FLUID, ForgeRegistries.FLUIDS.getKey(type).toString());
        if (type != EMPTY) stack.getOrCreateTag().putInt(COLOR, ResourceUtil.getColor(getType(stack)));
        else stack.getOrCreateTag().putInt(COLOR, -1);
        stack.getOrCreateTag().putInt(MAX, m);
        if (type != EMPTY) stack.getOrCreateTag().putInt(CURRENT, c);
        else stack.getOrCreateTag().putInt(CURRENT, 0);
        return stack;
    }

    public static Fluid getType(ItemStack stack){
        if (stack.getOrCreateTag().contains(FLUID)){
            ResourceLocation fluidName = new ResourceLocation(stack.getOrCreateTag().getString(FLUID));
            return Objects.requireNonNullElse(ForgeRegistries.FLUIDS.getValue(fluidName), EMPTY);
        }
        return EMPTY;
    }

    public void setAmount(ItemStack stack, int v, Fluid fluid) {
        if (v >= 0 && v <= getMaxValue(stack)) stack.getOrCreateTag().putInt(CURRENT, v);
        if (v <= 0) stack.getOrCreateTag().putString(FLUID, ForgeRegistries.FLUIDS.getKey(EMPTY).toString());
        else {
            stack.getOrCreateTag().putString(FLUID, ForgeRegistries.FLUIDS.getKey(fluid).toString());
            stack.getOrCreateTag().putInt(COLOR, ResourceUtil.getColor(fluid));
        }
    }

    public static int getValue(ItemStack stack){
        return stack.getOrCreateTag().getInt(CURRENT);
    }

    public static int getMaxValue(ItemStack stack){
        return stack.getOrCreateTag().getInt(MAX);
    }

    public static float getDisplayValue(ItemStack stack){
        if (stack.getOrCreateTag().contains(CURRENT) && stack.getOrCreateTag().contains(MAX))
            return (float) (stack.getOrCreateTag().getInt(CURRENT)) / stack.getOrCreateTag().getInt(MAX) * 6.0f;
        return 0;
    }

    public FluidStack getFluid(ItemStack stack) {
        return new FluidStack(getType(stack), getValue(stack));
    }

    public void setFluid(ItemStack item, FluidStack fluid) {
        item.getOrCreateTag().putString(FLUID, ForgeRegistries.FLUIDS.getKey(fluid.getFluid()).toString());
        item.getOrCreateTag().putInt(CURRENT, fluid.getAmount());
        item.getOrCreateTag().putInt(COLOR, ResourceUtil.getColor(fluid.getFluid()));
    }

    public static boolean increaseFluid(ItemStack item, FluidStack f) {
        if (getType(item) == EMPTY || getType(item).isSame(f.getFluid())) {
            if (getType(item) != f.getFluid()) {
                item.getOrCreateTag().putString(FLUID, ForgeRegistries.FLUIDS.getKey(f.getFluid()).toString());
                item.getOrCreateTag().putInt(COLOR, ResourceUtil.getColor(f.getFluid()));
            }
            item.getOrCreateTag().putInt(CURRENT, getValue(item) + f.getAmount());
            return true;
        }
        return false;
    }

    public static void copy(ItemStack stack, ItemStack item){
        stack.getOrCreateTag().putString(FLUID, ForgeRegistries.FLUIDS.getKey(getType(item)).toString());
        stack.getOrCreateTag().putInt(CURRENT, getValue(item));
        stack.getOrCreateTag().putInt(MAX, getMaxValue(item));
        stack.getOrCreateTag().putInt(COLOR, ResourceUtil.getColor(getType(item)));
    }

    public ItemStack create(FluidStack stack) {
        return create(stack.getAmount(), 8000, stack.getFluid(), ModInit.CANISTER.get());
    }

    public ItemStack createEmpty() {
        return create(0, 8000, EMPTY, ModInit.CANISTER.get());
    }

    public int getColor(ItemStack stack) {
        return stack.getOrCreateTag().getInt(COLOR);
    }

    public int getCapacity(ItemStack stack) {
        return getMaxValue(stack);
    }

    public int getOutlineColor() {
        return -1;
    }
}
