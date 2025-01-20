package mod.syconn.swe.util.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.swe.extra.core.FluidHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.Objects;

public record FluidHolderComponent(FluidHolder fluidHolder, int capacity) {

    public static FluidHolderComponent EMPTY = of(FluidHolder.EMPTY, 8000);

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidHolderComponent> STREAM_CODEC = StreamCodec.composite(
            FluidHolder.OPTIONAL_STREAM_CODEC, FluidHolderComponent::fluidHolder, ByteBufCodecs.INT, FluidHolderComponent::capacity, FluidHolderComponent::new
    );

    public static final Codec<FluidHolderComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            FluidHolder.OPTIONAL_CODEC.fieldOf("fluidHolder").forGetter(FluidHolderComponent::fluidHolder),
            Codec.INT.fieldOf("capacity").forGetter(FluidHolderComponent::capacity)
    ).apply(builder, FluidHolderComponent::new));

    public static FluidHolderComponent of(Fluid fluid, int amount, int capacity) {
        return of(new FluidHolder(fluid, amount), capacity);
    }

    public static FluidHolderComponent of(FluidHolder fluidHolder, int capacity) {
        if (fluidHolder.getAmount() <= 0) new FluidHolderComponent(FluidHolder.EMPTY, capacity);
        return new FluidHolderComponent(fluidHolder, capacity);
    }
    
    public static void updateFluidHolder(ItemStack stack, FluidHolder fluidHolder) {
        stack.set(ComponentRegister.FLUID_HOLDER_COMPONENT.get(), Objects.requireNonNull(stack.get(ComponentRegister.FLUID_HOLDER_COMPONENT.get())).setFluidHolder(fluidHolder));
    }

    public FluidHolderComponent setFluidHolder(FluidHolder fluidHolder) {
        return of(fluidHolder, capacity);
    }
}