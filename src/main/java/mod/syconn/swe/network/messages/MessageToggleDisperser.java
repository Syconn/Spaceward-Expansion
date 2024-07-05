package mod.syconn.swe.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageToggleDisperser implements IMessage<MessageToggleDisperser> {

    private BlockPos pos;

    public MessageToggleDisperser() { }

    public MessageToggleDisperser(BlockPos pos) {
        this.pos = pos;
    }

    public void encode(MessageToggleDisperser message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
    }

    public MessageToggleDisperser decode(FriendlyByteBuf buffer) {
        return new MessageToggleDisperser(buffer.readBlockPos());
    }

    public void handle(MessageToggleDisperser message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            if (supplier.get().getSender() != null) {
                supplier.get().getSender().level.getBlockEntity(message.pos, ModBlockEntity.DISPERSER.get()).get().toggleEnabled();
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
