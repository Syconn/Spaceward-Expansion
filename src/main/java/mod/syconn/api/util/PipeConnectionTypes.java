package mod.syconn.api.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PipeConnectionTypes implements StringRepresentable {
    INPUT,
    OUTPUT,
    BOTH,
    CABLE,
    BLOCK,
    NONE;

    public boolean isInteractionPoint() {
        return this == INPUT || this == OUTPUT || this == BOTH;
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
}
