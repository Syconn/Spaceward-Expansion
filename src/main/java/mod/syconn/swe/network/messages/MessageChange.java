package mod.syconn.swe.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import mod.syconn.swe.blockentities.PipeBlockEntity;

import java.util.function.Supplier;

public class MessageChange implements IMessage<MessageChange> {

    private BlockPos pos;

    public MessageChange() {}

    public MessageChange(BlockPos pos) { this.pos = pos; }

    @Override
    public void encode(MessageChange message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
    }

    @Override
    public MessageChange decode(FriendlyByteBuf buffer) {
        return new MessageChange(buffer.readBlockPos());
    }

    @Override
    public void handle(MessageChange message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayer player = supplier.get().getSender();
            if (player.level.getBlockEntity(message.pos) instanceof PipeBlockEntity pe) {
                pe.changeType();
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
