package mod.syconn.api.world.packets;

import io.netty.buffer.ByteBuf;
import mod.syconn.api.Constants;
import mod.syconn.api.world.capability.APICapabilities;
import mod.syconn.api.world.capability.IFluidHandlerInteractable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerBoundInteractableButtonPress(BlockPos pos, Direction side, IFluidHandlerInteractable.Interaction interaction) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerBoundInteractableButtonPress> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.ID, "interactable_button_press"));
    public static final StreamCodec<ByteBuf, ServerBoundInteractableButtonPress> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ServerBoundInteractableButtonPress::pos, Direction.STREAM_CODEC, ServerBoundInteractableButtonPress::side, IFluidHandlerInteractable.Interaction.STREAM_CODEC, ServerBoundInteractableButtonPress::interaction, ServerBoundInteractableButtonPress::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerBoundInteractableButtonPress message, IPayloadContext context) {
        context.enqueueWork(() -> {
            IFluidHandlerInteractable handler = context.player().level().getCapability(APICapabilities.FluidHandler.BLOCK, message.pos, message.side);
            if (handler != null) handler.setSideInteraction(message.side, message.interaction);
        });
    }
}
