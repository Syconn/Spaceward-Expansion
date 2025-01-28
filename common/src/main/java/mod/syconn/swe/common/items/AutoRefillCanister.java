package mod.syconn.swe.common.items;

import mod.syconn.swe.server.reloaders.PlanetManager;
import mod.syconn.swe.core.ModFluids;
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
            if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.wearingSpaceSuit(player)) {
                if (getHandler(stack).getFluidHolder().is(Fluids.EMPTY) || getHandler(stack).getFluidHolder().is(ModFluids.O2.get())) getHandler(stack).fill(new FluidHolder(ModFluids.O2.get(), 1), FluidAction.EXECUTE);
            }
        }
    }

    public void equipmentTick(ItemStack stack, Level level, Player player) {
        super.equipmentTick(stack, level, player);
        if (!level.isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.wearingSpaceSuit(player)) {
            if (getHandler(stack).getFluidHolder().is(Fluids.EMPTY) || getHandler(stack).getFluidHolder().is(ModFluids.O2.get())) getHandler(stack).fill(new FluidHolder(ModFluids.O2.get(), 1), FluidAction.EXECUTE);
        }
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(148, 135, 63);
    }
}
