package mod.syconn.swe;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static final ModConfigSpec CLIENT_CONFIG;
    public static ModConfigSpec.BooleanValue showOxygen;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        setupConfig(configBuilder);
        CLIENT_CONFIG = configBuilder.build();
    }

    private static void setupConfig(ModConfigSpec.Builder builder) {
        builder.push("Dev Config Options");
        showOxygen = builder.define("Render Oxygen Block", false);
        builder.pop();
    }
}
