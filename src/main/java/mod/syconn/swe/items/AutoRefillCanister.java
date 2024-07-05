package mod.syconn.swe.items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import mod.syconn.swe.util.data.AirBubblesSavedData;

import static net.minecraft.world.level.material.Fluids.EMPTY;

public class AutoRefillCanister extends Canister {

    public AutoRefillCanister() {
        super(Rarity.RARE);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity e, int p_41407_, boolean p_41408_) {
        if (e instanceof Player player) {
            if (!level.isClientSide && AirBubblesSavedData.get().breathable(level.dimension(), player.getOnPos().above(1)) && SpaceArmor.hasFullKit(player)) {
                if (getType(stack) == Fluids.EMPTY || getType(stack) == ModFluids.SOURCE_O2_FLUID.get() && getValue(stack) < getMaxValue(stack)) {
                    increaseFluid(stack, new FluidStack(ModFluids.SOURCE_O2_FLUID.get(), 1));
                }
            }
        }
    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
        super.onEquipmentTick(stack, level, player);
        if (!level.isClientSide && AirBubblesSavedData.get().breathable(level.dimension(), player.getOnPos().above(1)) && SpaceArmor.hasFullKit(player)) {
            if (getType(stack) == Fluids.EMPTY || getType(stack) == ModFluids.SOURCE_O2_FLUID.get() && getValue(stack) < getMaxValue(stack)) {
                increaseFluid(stack, new FluidStack(ModFluids.SOURCE_O2_FLUID.get(), 1));
            }
        }
    }

    public ItemStack create(FluidStack stack) {
        return create(stack.getAmount(), 8000, stack.getFluid(), ModInit.AUTO_REFILL_CANISTER.get());
    }

    public ItemStack createEmpty() {
        return create(0, 8000, EMPTY, ModInit.AUTO_REFILL_CANISTER.get());
    }

    @Override
    public int getOutlineColor() {
        return 0xA3954D;
    }
}
