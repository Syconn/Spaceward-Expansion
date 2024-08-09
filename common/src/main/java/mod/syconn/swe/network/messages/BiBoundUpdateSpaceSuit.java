package mod.syconn.swe.network.messages;

import io.netty.buffer.ByteBuf;
import mod.syconn.swe.Registration;
import mod.syconn.swe.common.data.attachments.SpaceSuit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BiBoundUpdateSpaceSuit(CompoundTag tag) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<BiBoundUpdateSpaceSuit> TYPE = new CustomPacketPayload.Type<>(Main.loc("update_space_siot"));
    public static final StreamCodec<ByteBuf, BiBoundUpdateSpaceSuit> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.COMPOUND_TAG, BiBoundUpdateSpaceSuit::tag,BiBoundUpdateSpaceSuit::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BiBoundUpdateSpaceSuit message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            SpaceSuit spaceSuit = player.getData(Registration.SPACE_SUIT);
            player.setData(Registration.SPACE_SUIT, spaceSuit.readSyncedData(spaceSuit, message.tag));
        });
    }
}
