package mod.syconn.swe.world.dimensions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import mod.syconn.swe.Main;

import java.util.HashMap;
import java.util.Map;

public class DimSettingsManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, DimensionSettings> SETTINGS = new HashMap<>();

    public DimSettingsManager() {
        super(GSON, "dim_settings");
    }

    protected void apply(Map<ResourceLocation, JsonElement> pJsonMap, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        pJsonMap.forEach(((resourceLocation, jsonElement) -> SETTINGS.put(resourceLocation, DimensionSettings.fromGson(jsonElement.getAsJsonObject()))));
    }

    public static DimensionSettings getSettings(ResourceKey<Level> k){
        if (SETTINGS.containsKey(k.location())) return SETTINGS.get(k.location());
        return SETTINGS.get(Main.loc("default"));
    }

    public static DimensionSettings getSettings(Player p){
        if (SETTINGS.containsKey(p.level().dimension().location())) return SETTINGS.get(p.level().dimension().location());
        return SETTINGS.get(Main.loc("default"));
    }
}
