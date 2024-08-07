package mod.syconn.swe.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public record FluidComponent(ResourceLocation fluidSrc, int amount, int capacity) {

    public static FluidComponent EMPTY = of(Fluids.EMPTY, 0, 0);

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidComponent> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, FluidComponent::fluidSrc, ByteBufCodecs.INT, FluidComponent::amount, ByteBufCodecs.INT, FluidComponent::capacity, FluidComponent::new
    );

    public static final Codec<FluidComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("fluid_src").forGetter(o -> o.fluidSrc), Codec.INT.fieldOf("amount").forGetter(o -> o.amount), Codec.INT.fieldOf("capacity").forGetter(o -> o.capacity)
    ).apply(builder, FluidComponent::new));

    public static FluidComponent of(Fluid fluid, int amount, int capacity) {
        if (amount <= 0) new FluidComponent(BuiltInRegistries.FLUID.getKey(Fluids.EMPTY), 0, capacity);
        return new FluidComponent(BuiltInRegistries.FLUID.getKey(fluid), amount, capacity);
    }

    public Fluid fluid() {
        return BuiltInRegistries.FLUID.get(fluidSrc);
    }

    public FluidComponent setFluid(Fluid fluid) {
        return new FluidComponent(BuiltInRegistries.FLUID.getKey(fluid), amount, capacity);
    }

    public FluidComponent setAmount(int amount) {
        return new FluidComponent(fluidSrc, amount, capacity);
    }

    public FluidComponent setCapacity(int capacity) {
        return new FluidComponent(fluidSrc, amount, capacity);
    }
}
