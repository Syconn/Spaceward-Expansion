package mod.syconn.swe;

import net.neoforged.neoforge.common.ModConfigSpec;

public class NeoConfig {

    public static final ModConfigSpec CLIENT_CONFIG;
    public static ModConfigSpec.BooleanValue showOxygen;
    public static ModConfigSpec.BooleanValue showPipeNetworks;

    public static final ModConfigSpec COMMON_CONFIG;
    public static ModConfigSpec.IntValue spaceHeight;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        setupClient(configBuilder);
        CLIENT_CONFIG = configBuilder.build();
        configBuilder = new ModConfigSpec.Builder();
        setupCommon(configBuilder);
        COMMON_CONFIG = configBuilder.build();
    }

    private static void setupClient(ModConfigSpec.Builder builder) {
        builder.push("Dev Config Options");
        showOxygen = builder.define("Render Oxygen Block", false);
        showPipeNetworks = builder.define("Render Debug for Fluid Pipes", false);
        builder.pop();
    }

    private static void setupCommon(ModConfigSpec.Builder builder) {
        builder.push("General Settings");
        spaceHeight = builder.defineInRange("Height to Enter Space", 500, 350, Integer.MAX_VALUE);
        builder.pop();
    }
}
