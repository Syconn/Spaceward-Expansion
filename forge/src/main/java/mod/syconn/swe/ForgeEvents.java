package mod.syconn.swe;

import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.blockentities.InteractableFluidHolderBlock;
import mod.syconn.swe.common.blockentities.forge.FluidHolderBlockImpl;
import mod.syconn.swe.common.blockentities.forge.InteractableFluidHolderBlockImpl;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.common.items.forge.FluidHolderItemImpl;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MOD)
public class ForgeEvents {

    static void onAttachBlockCapability(AttachCapabilitiesEvent<BlockEntity> event) {
        if(event.getObject() instanceof FluidHolderBlock.IFluidHolderBlock block)
            event.addCapability(Constants.withId("fluid_holder_block"), new FluidHolderBlockProvider(block));
        if(event.getObject() instanceof InteractableFluidHolderBlock.IInteractableFluidHolderBlock block)
            event.addCapability(Constants.withId("interactable_fluid_holder_block"), new FluidHolderBlockProvider(block));
    }

    static void onAttachItemCapability(AttachCapabilitiesEvent<ItemStack> event) {
        if(event.getObject().getItem() instanceof FluidHolderItem.IFluidHolderItem item)
            event.addCapability(Constants.withId("fluid_holder_item"), new FluidHolderItemProvider(event.getObject(), item.getCapacity()));
    }


    private static class FluidHolderBlockProvider implements ICapabilityProvider {
        final LazyOptional<IFluidHandler> holder;

        public FluidHolderBlockProvider(FluidHolderBlock.IFluidHolderBlock block) {
            this.holder = LazyOptional.of(() -> {
                FluidHolderBlock fluidHolder = block.getFluidHolder();
                if(fluidHolder == null) return EmptyFluidHandler.INSTANCE;
                return ((FluidHolderBlockImpl.ForgeFluidHolderBlock) fluidHolder).getTank();
            });
        }

        public FluidHolderBlockProvider(InteractableFluidHolderBlock.IInteractableFluidHolderBlock block) {
            this.holder = LazyOptional.of(() -> {
                FluidHolderBlock fluidHolder = block.getFluidHolder();
                if(fluidHolder == null) return EmptyFluidHandler.INSTANCE;
                return ((InteractableFluidHolderBlockImpl.ForgeInteractableFluidHolderBlock) fluidHolder).getTank();
            });
        }

        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap == ForgeCapabilities.FLUID_HANDLER ? this.holder.cast() : LazyOptional.empty();
        }
    }

    private static class FluidHolderItemProvider implements ICapabilityProvider {
        final LazyOptional<IFluidHandlerItem> holder;

        public FluidHolderItemProvider(ItemStack stack, long capacity) {
            this.holder = LazyOptional.of(() -> new FluidHolderItemImpl.ForgeFluidHolderItem(stack, (int) capacity));
        }

        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap == ForgeCapabilities.FLUID_HANDLER_ITEM ? this.holder.cast() : LazyOptional.empty();
        }
    }
}
