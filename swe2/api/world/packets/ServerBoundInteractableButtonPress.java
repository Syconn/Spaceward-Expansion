package mod.syconn.swe2.api.world.packets;

import io.netty.buffer.ByteBuf;
import mod.syconn.swe2.api.Constants;
import mod.syconn.swe2.api.world.data.capability.APICapabilities;
import mod.syconn.swe2.api.world.data.capability.IFluidHandlerInteractable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerBoundInteractableButtonPress(BlockPos pos, Direction side, IFluidHandlerInteractable.Interaction interaction) implements CustomPacketPayload {

    public static final Type<ServerBoundInteractableButtonPress> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.ID, "interactable_button_press"));
    public static final StreamCodec<ByteBuf, ServerBoundInteractableButtonPress> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ServerBoundInteractableButtonPress::pos, Direction.STREAM_CODEC, ServerBoundInteractableButtonPress::side, IFluidHandlerInteractable.Interaction.STREAM_CODEC, ServerBoundInteractableButtonPress::interaction, ServerBoundInteractableButtonPress::new);

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerBoundInteractableButtonPress message, IPayloadContext context) {
        context.enqueueWork(() -> {
            IFluidHandlerInteractable handler = context.player().level().getCapability(APICapabilities.FluidHandler.BLOCK, message.pos, message.side);
            if (handler != null) handler.setSideInteraction(message.side, message.interaction);
        });
    }
}
