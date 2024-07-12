package mod.syconn.swe.world.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.swe.client.RenderUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public record CanisterComponent(FluidStack fluidType, int volume, int max, int color) {

    public static final CanisterComponent DEFAULT = new CanisterComponent(FluidStack.EMPTY, 0, 0, -1);
    public static final Codec<CanisterComponent> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FluidStack.OPTIONAL_CODEC.fieldOf("fluidType").forGetter(CanisterComponent::fluidType),
                    Codec.INT.fieldOf("volume").forGetter(CanisterComponent::volume),
                    Codec.INT.fieldOf("max").forGetter(CanisterComponent::max),
                    Codec.INT.fieldOf("color").forGetter(CanisterComponent::color)
            ).apply(instance, CanisterComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CanisterComponent> BASIC_STREAM_CODEC = StreamCodec.composite(
            FluidStack.OPTIONAL_STREAM_CODEC, CanisterComponent::fluidType,
            ByteBufCodecs.INT, CanisterComponent::volume,
            ByteBufCodecs.INT, CanisterComponent::max,
            ByteBufCodecs.INT, CanisterComponent::color,
            CanisterComponent::new
    );

    public FluidStack fluidStack() {
        return fluidType.copyWithAmount(volume);
    }

    public CanisterComponent set(FluidStack fluid) {
        return new CanisterComponent(fluid.copyWithAmount(1), Math.min(fluid.getAmount(), max), max, fluid.is(Fluids.EMPTY) ? -1 : RenderUtil.getFluidColor(fluid));
    }

    public CanisterComponent increase(FluidStack fluid) {
        return new CanisterComponent(fluid.copyWithAmount(1), Math.min(volume + fluid.getAmount(), max), max, color);
    }
}
