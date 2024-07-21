package mod.syconn.swe.fluids;

import mod.syconn.swe.Registration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class O2Fluid extends BaseFlowingFluid {

    public static final BaseFlowingFluid.Properties PROPERTIES = new BaseFlowingFluid.Properties(Registration.O2_FLUID_TYPE, Registration.O2, Registration.O2_FLOWING)
            .block(Registration.O2_FLUID_BLOCK).bucket(Registration.O2_BUCKET).slopeFindDistance(3);

    public O2Fluid(Properties properties) {
        super(properties);
    }
}