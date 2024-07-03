package mod.syconn.swe.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import mod.syconn.swe.common.be.PipeBlockEntity;

import java.util.function.Supplier;

public class MessageClickTab implements IMessage<MessageClickTab> {

    private BlockPos pos;
    private Direction d;

    public MessageClickTab() {}

    public MessageClickTab(BlockPos pos, Direction d) {
        this.pos = pos;
        this.d = d;
    }

    @Override
    public void encode(MessageClickTab message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeInt(message.d.get3DDataValue());
    }

    @Override
    public MessageClickTab decode(FriendlyByteBuf buffer) {
        return new MessageClickTab(buffer.readBlockPos(), Direction.from3DDataValue(buffer.readInt()));
    }

    @Override
    public void handle(MessageClickTab message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            ServerPlayer player = supplier.get().getSender();
            if (player.level.getBlockEntity(message.pos) instanceof PipeBlockEntity pe) {
                pe.setTarget(message.d);
                NetworkHooks.openScreen(player, pe, message.pos);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
