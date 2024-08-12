package mod.syconn.swe.items;

import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.init.FluidRegister;
import mod.syconn.swe.init.ItemRegister;
import mod.syconn.swe.platform.services.ISingleFluidHandler;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

import static net.minecraft.world.level.material.Fluids.EMPTY;

public class AutoRefillCanister extends Canister {

    public AutoRefillCanister() {
        super(Rarity.RARE);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity e, int p_41407_, boolean p_41408_) {
        if (e instanceof Player player) {
            if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.hasFullKit(player)) {
                if (getHandler(stack).getFluidInTank().is(Fluids.EMPTY) || getHandler(stack).getFluidInTank().is(FluidRegister.O2.get())) getHandler(stack).fill(new ISingleFluidHandler.FluidHolder(FluidRegister.O2.get(), 1), ISingleFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
        super.onEquipmentTick(stack, level, player);
        if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.hasFullKit(player)) {
            if (getHandler(stack).getFluidInTank().is(Fluids.EMPTY) || getHandler(stack).getFluidInTank().is(FluidRegister.O2.get())) getHandler(stack).fill(new ISingleFluidHandler.FluidHolder(FluidRegister.O2.get(), 1), ISingleFluidHandler.FluidAction.EXECUTE);
        }
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(148, 135, 63);
    }
}
