package mod.syconn.swe.world.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;

public record CanisterComponent(FluidStack fluid, int max, int color) {

    public static final CanisterComponent DEFAULT = new CanisterComponent(FluidStack.EMPTY, 0, 0);

    public CanisterComponent set(FluidStack fluid) {
        return new CanisterComponent(fluid, Math.min(fluid.getAmount(), max), max);
    }

    public static final Codec<CanisterComponent> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FluidStack.OPTIONAL_CODEC.fieldOf("fluid").forGetter(CanisterComponent::fluid),
                    Codec.INT.fieldOf("max").forGetter(CanisterComponent::max),
                    Codec.INT.fieldOf("color").forGetter(CanisterComponent::color)
            ).apply(instance, CanisterComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CanisterComponent> BASIC_STREAM_CODEC = StreamCodec.composite(
            FluidStack.OPTIONAL_STREAM_CODEC, CanisterComponent::fluid,
            ByteBufCodecs.INT, CanisterComponent::max,
            ByteBufCodecs.INT, CanisterComponent::color,
            CanisterComponent::new
    );
}
