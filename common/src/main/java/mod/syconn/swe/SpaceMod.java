package mod.syconn.swe;

import mod.syconn.swe.init.*;
import mod.syconn.swe.network.Network;

public class SpaceMod {

    public static void init() {
        Network.registerMessages();
        ComponentRegister.init();
        BlockRegister.init();
        BlockEntityRegister.init();
        MenuRegister.init();
        FluidRegister.init();
        ItemRegister.init();
    }
}