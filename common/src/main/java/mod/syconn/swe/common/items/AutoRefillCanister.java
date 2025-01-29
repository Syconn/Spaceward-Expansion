package mod.syconn.swe.common.items;

import dev.architectury.fluid.FluidStack;
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

    public void equipmentTick(ItemStack stack, Player player) {
        super.equipmentTick(stack, player);
        if (!player.level().isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.wearingSpaceSuit(player)) {
            if (getFluidStack(stack).getFluid().isSame(Fluids.EMPTY) || getFluidStack(stack).getFluid().isSame(ModFluids.O2.get()))
                fill(stack, FluidStack.create(ModFluids.O2.get(), 1), false);
        }
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(-1, 148, 135, 63);
    }
}
