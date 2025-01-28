package mod.syconn.swe.network.messages;

import mod.syconn.swe.util.InteractionalFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;

public record ServerBoundInteractableButtonPress(BlockPos pos, Direction side, InteractionalFluidHandler.Interaction interaction) {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerBoundInteractableButtonPress> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ServerBoundInteractableButtonPress::pos, Direction.STREAM_CODEC, ServerBoundInteractableButtonPress::side, InteractionalFluidHandler.Interaction.STREAM_CODEC, ServerBoundInteractableButtonPress::interaction, ServerBoundInteractableButtonPress::new);

    public static void handle(ServerBoundInteractableButtonPress message, Player player) {
        InteractionalFluidHandler handler = Services.FLUID_HANDLER.getInteractional(player.level(), message.pos, message.side);
        if (handler != null) handler.setSideInteraction(message.side, message.interaction);
    }
}
