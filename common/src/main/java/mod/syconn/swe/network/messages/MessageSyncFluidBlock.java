package mod.syconn.swe.network.messages;

import dev.architectury.fluid.FluidStack;
import dev.architectury.networking.NetworkManager;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.function.Supplier;

public class MessageSyncFluidBlock {

    private final BlockPos pos;
    private final FluidStack fluidStack;

    public MessageSyncFluidBlock(BlockPos pos, FluidStack fluidStack) {
        this.pos = pos;
        this.fluidStack = fluidStack;
    }

    public MessageSyncFluidBlock(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), FluidStack.read(buf));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        this.fluidStack.write(buf);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            Minecraft mc = Minecraft.getInstance();
            Level level = Objects.requireNonNull(mc.level);
            if(level.getBlockEntity(pos) instanceof FluidHolderBlock.IFluidHolderBlock be) {
                FluidHolderBlock container = be.getFluidHolder();
                if(container != null) container.handleSync(level, fluidStack);
            }
        });
    }
}
