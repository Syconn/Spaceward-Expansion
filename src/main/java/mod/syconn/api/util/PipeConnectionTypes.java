package mod.syconn.api.util;

import net.minecraft.util.StringRepresentable;

public enum PipeConnectionTypes implements StringRepresentable { // TODO MAKE BETTER FOR MULIPIPE BLOCKS
    INPUT,
    OUTPUT,
    CABLE,
    BLOCK,
    NONE;

    public String getSerializedName() {
        return name().toLowerCase();
    }
}
