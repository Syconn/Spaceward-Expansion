package mod.syconn.swe.init;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import mod.syconn.swe.Main;
import mod.syconn.swe.capabilities.ISpaceSuit;
import mod.syconn.swe.capabilities.SpaceSuit;

@Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class ModCapabilities {

    public static final Capability<ISpaceSuit> SPACE_SUIT = CapabilityManager.get(new CapabilityToken<>() {});

    private static class SpaceSuitCap implements ICapabilitySerializable<CompoundTag> {

        private final ISpaceSuit backend = new SpaceSuit();
        private final LazyOptional<ISpaceSuit> optionalData = LazyOptional.of(() -> backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return SPACE_SUIT.orEmpty(cap, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    @SubscribeEvent
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(new ResourceLocation(Main.MODID, "spacesuit"), new SpaceSuitCap());
    }
}
