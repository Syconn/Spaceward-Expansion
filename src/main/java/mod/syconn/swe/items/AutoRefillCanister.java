package mod.syconn.swe.items;

import mod.syconn.swe.Registration;
import mod.syconn.swe.world.dimensions.PlanetManager;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

import static net.minecraft.world.level.material.Fluids.EMPTY;

public class AutoRefillCanister extends Canister {

    public AutoRefillCanister() {
        super(Rarity.RARE);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity e, int p_41407_, boolean p_41408_) {
        if (e instanceof Player player) {
            if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.hasFullKit(player)) {
                if (get(stack).fluidType().is(Fluids.EMPTY) || get(stack).fluidType().is(Registration.O2.get()) && get(stack).volume() < get(stack).max())
                    increaseFluid(stack, new FluidStack(Registration.O2.get(), 1));
            }
        }
    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
        super.onEquipmentTick(stack, level, player);
        if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.hasFullKit(player)) {
            if (get(stack).fluidType().is(EMPTY) || get(stack).fluidType().is(Registration.O2.get()) && get(stack).volume() < get(stack).max())
                increaseFluid(stack, new FluidStack(Registration.O2.get(), 1));
        }
    }

    public ItemStack create(FluidStack stack) {
        return create(stack.getAmount(), 8000, stack.getFluid(), Registration.AUTO_REFILL_CANISTER.get());
    }

    public ItemStack createEmpty() {
        return create(0, 8000, EMPTY, Registration.AUTO_REFILL_CANISTER.get());
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(148, 135, 63);
    }
}
