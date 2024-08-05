package mod.syconn.swe2.api.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PipeConnectionTypes implements StringRepresentable {
    INPUT(0),
    OUTPUT(1),
    BOTH(2),
    CABLE(3),
    BLOCK(4),
    NONE(5);

    public static final StreamCodec<ByteBuf, PipeConnectionTypes> STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(PipeConnectionTypes::getID, values(), ByIdMap.OutOfBoundsStrategy.WRAP), PipeConnectionTypes::getID);

    final int id;

    PipeConnectionTypes(int id) {
        this.id = id;
    }

    public boolean isInteractionPoint() {
        return this == INPUT || this == OUTPUT || this == BOTH || this == BLOCK;
    }

    public boolean isImport() {
        return this == INPUT || this == BOTH;
    }

    public boolean isExport() {
        return this == OUTPUT || this == BOTH;
    }

    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }

    public int getID() {
        return id;
    }
}
