package mod.syconn.swe.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import mod.syconn.swe.blockentities.PipeBlockEntity;

import java.util.function.Supplier;

public class MessageClickArrow implements IMessage<MessageClickArrow> {

    private BlockPos pos;
    private int inc;

    public MessageClickArrow() {}

    public MessageClickArrow(BlockPos pos, int inc) {
        this.pos = pos;
        this.inc = inc;
    }

    @Override
    public void encode(MessageClickArrow message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeInt(message.inc);
    }

    @Override
    public MessageClickArrow decode(FriendlyByteBuf buffer) {
        return new MessageClickArrow(buffer.readBlockPos(), buffer.readInt());
    }

    @Override
    public void handle(MessageClickArrow message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayer player = supplier.get().getSender();
            if (player.level.getBlockEntity(message.pos) instanceof PipeBlockEntity pe) {
                pe.increment(message.inc);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
