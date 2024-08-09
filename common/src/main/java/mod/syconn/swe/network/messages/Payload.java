package mod.syconn.swe.network.messages;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record Payload<T>(Type<?> type, T msg) implements CustomPacketPayload  {

    public static <T, B> StreamCodec<B, Payload<T>> codec(Type<?> type, StreamCodec<B, T> codec) {
        return StreamCodec.composite(codec, Payload::msg, msg -> new Payload<>(type, msg));
    }

    public Type<?> type() {
        return this.type;
    }
}
