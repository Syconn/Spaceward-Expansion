package mod.syconn.swe2.network.messages;

import mod.syconn.swe2.Main;
import mod.syconn.swe2.world.dimensions.PlanetManager;
import mod.syconn.swe2.world.dimensions.PlanetSettings;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ClientBoundUpdatePlanetSettings(List<PlanetSettings> settings) implements CustomPacketPayload {

    public static final Type<ClientBoundUpdatePlanetSettings> TYPE = new Type<>(Main.loc("update_planet_settings"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundUpdatePlanetSettings> STREAM_CODEC = StreamCodec.composite(
            PlanetSettings.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientBoundUpdatePlanetSettings::settings, ClientBoundUpdatePlanetSettings::new
    );

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientBoundUpdatePlanetSettings message, IPayloadContext context) {
        context.enqueueWork(() -> PlanetManager.replaceSettings(message.settings));
    }
}
