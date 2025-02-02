package mod.syconn.swe.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import mod.syconn.swe.common.blockentities.InteractableFluidHolderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class MessageChangeInteractionSide {

    private final BlockPos pos;
    private final Direction side;
    private final InteractableFluidHolderBlock.Interaction interaction;

    public MessageChangeInteractionSide(BlockPos pos, Direction side, InteractableFluidHolderBlock.Interaction interaction) {
        this.pos = pos;
        this.side = side;
        this.interaction = interaction;
    }

    public MessageChangeInteractionSide(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readEnum(Direction.class), buf.readEnum(InteractableFluidHolderBlock.Interaction.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeEnum(this.side);
        buf.writeEnum(this.interaction);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            FluidHolderBlock handler = InteractableFluidHolderBlock.getOrWrapFluidHolder(context.get().getPlayer().level(), this.pos, this.side);
            if (handler instanceof InteractableFluidHolderBlock interactionHandler) interactionHandler.setSideInteraction(this.side, this.interaction);
        });
    }
}
