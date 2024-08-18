package mod.syconn.swe.network.messages;

import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.init.DataAttachments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public record BiBoundUpdateSpaceSuit(CompoundTag tag) {

    public static final StreamCodec<RegistryFriendlyByteBuf, BiBoundUpdateSpaceSuit> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.COMPOUND_TAG, BiBoundUpdateSpaceSuit::tag,BiBoundUpdateSpaceSuit::new);

    public static void handle(BiBoundUpdateSpaceSuit message, Player player) {
        Services.ATTACHED_DATA.update(DataAttachments.SPACE_SUIT, suit -> suit.readSyncedData(suit, message.tag), player);
    }
}
