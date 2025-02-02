package mod.syconn.swe.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swe.common.blockentities.FluidPipeBE;
import mod.syconn.swe.util.PipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class MessageUpdatePipeState {

    private final BlockPos pos;
    private final Direction side;
    private final PipeUtil.PipeConnectionTypes connection;

    public MessageUpdatePipeState(BlockPos pos, Direction side, PipeUtil.PipeConnectionTypes connection) {
        this.pos = pos;
        this.side = side;
        this.connection = connection;
    }

    public MessageUpdatePipeState(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readEnum(Direction.class), buf.readEnum(PipeUtil.PipeConnectionTypes.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeEnum(this.side);
        buf.writeEnum(this.connection);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer().level().getBlockEntity(this.pos) instanceof FluidPipeBE pipe) pipe.setConnectionType(this.side, this.connection);
        });
    }
}
