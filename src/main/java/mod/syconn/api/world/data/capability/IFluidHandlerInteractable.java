package mod.syconn.api.world.data.capability;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public interface IFluidHandlerInteractable extends IFluidHandler {

    enum Interaction {
        PUSH(0),
        PULL(1),
        BOTH(2),
        NONE(3);

        public static final StreamCodec<ByteBuf, Interaction> STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(Interaction::getI, values(), ByIdMap.OutOfBoundsStrategy.WRAP), Interaction::getI);

        int i;
        Interaction(int i) {
            this.i = i;
        }

        int getI() {
            return i;
        }

        static Interaction fromI(int i) {
            for (Interaction interaction : Interaction.values()) if (interaction.i == i) return interaction;
            return NONE;
        }

        boolean isPull() {
            return this == PULL || this == BOTH;
        }

        boolean isPush() {
            return this == PUSH || this == BOTH;
        }
    }

    Interaction getSideInteraction(Direction side);
    void setSideInteraction(Direction side, Interaction interaction);
    void handlePush(Level level, BlockPos blockPos);
    void handlePull(Level level, BlockPos blockPos);
}
