package mod.syconn.swe.extra.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.swe.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

/**
 * A Common Version of Forge/NeoForge's FluidStack class for Common Side Coding
 */
public class FluidHolder {

    public static FluidHolder EMPTY = new FluidHolder(Fluids.EMPTY);

    public static final Codec<Holder<Fluid>> FLUID_NON_EMPTY_CODEC = BuiltInRegistries.FLUID.holderByNameCodec().validate(holder -> holder.is(Fluids.EMPTY.builtInRegistryHolder()) ? DataResult.error(() -> "Fluid must not be minecraft:empty") : DataResult.success(holder));
    /**
     * A standard codec for fluid stacks that does not accept empty stacks.
     */
    public static final Codec<FluidHolder> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(
                    instance -> instance.group(FLUID_NON_EMPTY_CODEC.fieldOf("id").forGetter(FluidHolder::getFluidHolder), ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(FluidHolder::getAmount)).apply(instance, FluidHolder::new)));

    /**
     * A standard codec for fluid stacks that accepts empty stacks, serializing them as {@code {}}.
     */
    public static final Codec<FluidHolder> OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC).xmap(optional -> optional.orElse(FluidHolder.EMPTY), stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack));
    /**
     * A stream codec for fluid stacks that accepts empty stacks.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidHolder> OPTIONAL_STREAM_CODEC = new StreamCodec<>() {
        private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Fluid>> FLUID_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FLUID);

        public FluidHolder decode(RegistryFriendlyByteBuf buf) {
            int amount = buf.readVarInt();
            if (amount <= 0) {
                return FluidHolder.EMPTY;
            } else {
                Holder<Fluid> holder = FLUID_STREAM_CODEC.decode(buf);
                return new FluidHolder(holder, amount);
            }
        }

        public void encode(RegistryFriendlyByteBuf buf, FluidHolder stack) {
            if (stack.isEmpty()) {
                buf.writeVarInt(0);
            } else {
                buf.writeVarInt(stack.getAmount());
                FLUID_STREAM_CODEC.encode(buf, stack.getFluidHolder());
            }
        }
    };

    private Fluid fluid;
    private int amount;

    public FluidHolder(Fluid fluid) {
        this.fluid = fluid;
        this.amount = 0;
    }

    public FluidHolder(Holder<Fluid> fluid) {
        this.fluid = fluid.value();
        this.amount = 0;
    }

    public FluidHolder(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
        checkAmount();
    }

    public FluidHolder(Holder<Fluid> fluid, int amount) {
        this.fluid = fluid.value();
        this.amount = amount;
        checkAmount();
    }

    public static Optional<FluidHolder> parse(HolderLookup.Provider lookupProvider, Tag tag) {
        return CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial(error -> Constants.LOG.error("Tried to load invalid fluid: '{}'", error));
    }

    public static FluidHolder parseOptional(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        return tag.isEmpty() ? EMPTY : parse(lookupProvider, tag).orElse(EMPTY);
    }

    public Tag save(HolderLookup.Provider lookupProvider) {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty FluidStack");
        } else {
            return CODEC.encodeStart(lookupProvider.createSerializationContext(NbtOps.INSTANCE), this).getOrThrow();
        }
    }

    private void checkAmount() {
        if (amount <= 0) {
            fluid = Fluids.EMPTY;
            amount = 0;
        }
    }

    public Fluid getFluid() {
        return fluid;
    }

    public Holder<Fluid> getFluidHolder() {
        return this.getFluid().builtInRegistryHolder();
    }

    public int getAmount() {
        return amount;
    }

    public boolean isEmpty() {
        return this.amount == 0;
    }

    public boolean is(FluidHolder holder) {
        return this.fluid.isSame(holder.fluid);
    }

    public boolean is(Fluid fluid) {
        return this.fluid.isSame(fluid);
    }

    public boolean is(TagKey<Fluid> fluid) {
        return this.fluid.is(fluid);
    }

    public FluidHolder shrink(int drainAmount) {
        this.amount -= drainAmount;
        checkAmount();
        return this;
    }

    public FluidHolder fill(int fillAmount) {
        this.amount += fillAmount;
        checkAmount();
        return this;
    }

    public FluidHolder copyWith(int amount) {
        return new FluidHolder(fluid, amount);
    }

    public String toString() {
        return "FluidHolder{" + "fluid=" + fluid + ", amount=" + amount + '}';
    }
}
