package mod.syconn.swe2.util;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface BlockInfo {

    int getFluidRate();
    int getPowerRate();

    List<Component> getExtraInfo();
}
