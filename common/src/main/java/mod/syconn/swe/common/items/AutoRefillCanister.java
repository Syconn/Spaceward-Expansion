package mod.syconn.swe.common.items;

import dev.architectury.fluid.FluidStack;
import mod.syconn.swe.registry.ModFluids;
import mod.syconn.swe.server.reloaders.PlanetManager;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.Fluids;

public class AutoRefillCanister extends Canister {

    public AutoRefillCanister() {
        super(Rarity.RARE);
    }

    public void equipmentTick(ItemStack stack, Player player) {
        super.equipmentTick(stack, player);
        FluidHolderItem fluidHolder = getFluidHolder(player, player.containerMenu, stack);
        if (!player.level().isClientSide && PlanetManager.getSettings(player).breathable() && SpaceArmor.wearingSpaceSuit(player)) {
            if (fluidHolder.getFluidStack().getFluid().isSame(Fluids.EMPTY) ||fluidHolder.getFluidStack().getFluid().isSame(ModFluids.O2.get()))
                fluidHolder.push(FluidStack.create(ModFluids.O2.get(), 1), false);
        }
    }

    public int getOutlineColor() {
        return FastColor.ARGB32.color(-1, 148, 135, 63);
    }
}
