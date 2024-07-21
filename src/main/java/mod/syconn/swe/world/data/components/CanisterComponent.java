package mod.syconn.swe.world.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;

public record CanisterComponent(FluidStack fluidType, int volume, int max) {

    public static final CanisterComponent DEFAULT = new CanisterComponent(FluidStack.EMPTY, 0, 0);
    public static final Codec<CanisterComponent> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FluidStack.OPTIONAL_CODEC.fieldOf("fluidType").forGetter(CanisterComponent::fluidType),
                    Codec.INT.fieldOf("volume").forGetter(CanisterComponent::volume),
                    Codec.INT.fieldOf("max").forGetter(CanisterComponent::max)
            ).apply(instance, CanisterComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CanisterComponent> BASIC_STREAM_CODEC = StreamCodec.composite(
            FluidStack.OPTIONAL_STREAM_CODEC, CanisterComponent::fluidType,
            ByteBufCodecs.INT, CanisterComponent::volume,
            ByteBufCodecs.INT, CanisterComponent::max,
            CanisterComponent::new
    );

    public FluidStack fluidStack() {
        return fluidType.copyWithAmount(volume);
    }

    public CanisterComponent set(FluidStack fluid) {
        return new CanisterComponent(fluid.copyWithAmount(1), Math.min(fluid.getAmount(), max), max);
    }

    public CanisterComponent increase(FluidStack fluid) {
        return new CanisterComponent(fluid.copyWithAmount(1), Math.min(volume + fluid.getAmount(), max), max);
    }
}
