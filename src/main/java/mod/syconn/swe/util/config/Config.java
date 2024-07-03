package mod.syconn.swe.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec.BooleanValue showOxygen;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        CLIENT_CONFIG = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Dev Config Options");
        showOxygen = builder.define("Render Oxygen Block", false);
        builder.pop();
    }
}
