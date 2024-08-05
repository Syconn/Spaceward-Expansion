package mod.syconn.swe.items;

import mod.syconn.swe.init.ItemRegister;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import static net.minecraft.world.level.material.Fluids.EMPTY;

public class AutoRefillCanister extends Canister {

    public AutoRefillCanister() {
        super(Rarity.RARE);
    }

//    public void inventoryTick(ItemStack stack, Level level, Entity e, int p_41407_, boolean p_41408_) { TODO LATER
//        if (e instanceof Player player) {
//            if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.hasFullKit(player)) {
//                if (getHandler(stack).getFluidInTank(0).is(Fluids.EMPTY) || getHandler(stack).getFluidInTank(0).is(Registration.O2.get())) getHandler(stack).fill(new FluidStack(Registration.O2.get(), 1), IFluidHandler.FluidAction.EXECUTE);
//            }
//        }
//    }
//
//    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
//        super.onEquipmentTick(stack, level, player);
//        if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.hasFullKit(player)) {
//            if (getHandler(stack).getFluidInTank(0).is(Fluids.EMPTY) || getHandler(stack).getFluidInTank(0).is(Registration.O2.get())) getHandler(stack).fill(new FluidStack(Registration.O2.get(), 1), IFluidHandler.FluidAction.EXECUTE);
//        }
//    }


    public ItemStack createEmpty() {
        return create(0, 8000, EMPTY, ItemRegister.AUTO_REFILL_CANISTER.get());
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(148, 135, 63);
    }
}
