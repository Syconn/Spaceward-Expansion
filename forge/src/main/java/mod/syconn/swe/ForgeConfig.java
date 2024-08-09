package mod.syconn.swe;

import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeConfig {

    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec.BooleanValue showOxygen;
    public static ForgeConfigSpec.BooleanValue showPipeNetworks;

    public static final ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.IntValue spaceHeight;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupClient(configBuilder);
        CLIENT_CONFIG = configBuilder.build();
        configBuilder = new ForgeConfigSpec.Builder();
        setupCommon(configBuilder);
        COMMON_CONFIG = configBuilder.build();
    }

    private static void setupClient(ForgeConfigSpec.Builder builder) {
        builder.push("Dev Config Options");
        showOxygen = builder.define("Render Oxygen Block", false);
        showPipeNetworks = builder.define("Render Debug for Fluid Pipes", false);
        builder.pop();
    }

    private static void setupCommon(ForgeConfigSpec.Builder builder) {
        builder.push("General Settings");
        spaceHeight = builder.defineInRange("Height to Enter Space", 500, 350, Integer.MAX_VALUE);
        builder.pop();
    }
}
