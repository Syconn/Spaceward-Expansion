package mod.syconn.swe.common.dimensions;

import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record PlanetSettings(ResourceLocation location, double gravity, boolean breathable) {

    public static final StreamCodec<RegistryFriendlyByteBuf, PlanetSettings> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, PlanetSettings::location, ByteBufCodecs.DOUBLE, PlanetSettings::gravity, ByteBufCodecs.BOOL, PlanetSettings::breathable, PlanetSettings::new
    );

    public String toString() {
        return "Gravitational Force: " + gravity + " Breathable: " + breathable + "Saved @:" + location;
    }

    public static PlanetSettings fromGson(ResourceLocation location, JsonObject o){
        return new PlanetSettings(location, o.get("gravity").getAsDouble(), o.get("breathable").getAsBoolean());
    }
}
