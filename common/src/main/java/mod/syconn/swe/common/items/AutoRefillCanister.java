package mod.syconn.swe.common.items;

import mod.syconn.swe.server.reloaders.PlanetManager;
import mod.syconn.swe.core.FluidRegister;
import mod.syconn.swe.util.core.FluidHolder;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

public class AutoRefillCanister extends Canister {

    public AutoRefillCanister() {
        super(Rarity.RARE);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity e, int p_41407_, boolean p_41408_) {
        if (e instanceof Player player) {
            if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.hasFullKit(player)) {
                if (getHandler(stack).getFluidHolder().is(Fluids.EMPTY) || getHandler(stack).getFluidHolder().is(FluidRegister.O2.get())) getHandler(stack).fill(new FluidHolder(FluidRegister.O2.get(), 1), FluidAction.EXECUTE);
            }
        }
    }

    public void onEquipmentTick(ItemStack stack, Level level, Player player) {
        super.onEquipmentTick(stack, level, player);
        if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.hasFullKit(player)) {
            if (getHandler(stack).getFluidHolder().is(Fluids.EMPTY) || getHandler(stack).getFluidHolder().is(FluidRegister.O2.get())) getHandler(stack).fill(new FluidHolder(FluidRegister.O2.get(), 1), FluidAction.EXECUTE);
        }
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(148, 135, 63);
    }
}
