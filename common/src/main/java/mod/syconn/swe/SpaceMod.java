package mod.syconn.swe;

import mod.syconn.swe.init.ComponentRegister;
import mod.syconn.swe.init.ItemRegister;

public class SpaceMod {

    public static void init() {
        ComponentRegister.init();
        ItemRegister.init();
    }
}