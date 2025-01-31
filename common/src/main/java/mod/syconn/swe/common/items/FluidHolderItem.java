package mod.syconn.swe.common.items;

import dev.architectury.fluid.FluidStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class FluidHolderItem {

    public abstract FluidStack getFluidStack();
    public abstract boolean isEmpty();
    public abstract void setFluidStack(FluidStack fluidStack);

    /// Returns the remaining amount of fluid from the amount added
    public abstract long push(FluidStack fluidStack, boolean simulate);

    /// This method returns a pair containing the type of fluid and the amount that was removed.
    public abstract FluidStack pull(long amount, boolean simulate);

    @ExpectPlatform
    public static FluidHolderItem getFluidHolder(Player player, InteractionHand hand) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static FluidHolderItem getFluidHolder(Player player, @Nullable AbstractContainerMenu inventory, ItemStack stack) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static FluidHolderItem getFluidHolder(Container container, int slot, ItemStack stack) {
        throw new AssertionError();
    }

    public interface IFluidHolderItem {
        long getCapacity();
        default FluidHolderItem getFluidHolder(Player player, InteractionHand hand){
            return FluidHolderItem.getFluidHolder(player, hand);
        }

        default FluidHolderItem getFluidHolder(Player player, AbstractContainerMenu inventory, ItemStack stack){
            return FluidHolderItem.getFluidHolder(player, inventory, stack);
        }

        @Environment(EnvType.CLIENT)
        @Nullable
        default FluidHolderItem getOnClient(ItemStack stack) {
            return EnvExecutor.getInEnv(Env.CLIENT, () -> () -> {
                Player player = Minecraft.getInstance().player;
                if (player != null) return getFluidHolder(player, player.containerMenu, stack);
                return null;
            }).orElseThrow();
        }
    }
}
