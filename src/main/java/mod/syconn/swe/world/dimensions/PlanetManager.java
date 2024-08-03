package mod.syconn.swe2.world.dimensions;

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
import mod.syconn.swe2.Main;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlanetManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static Map<ResourceLocation, PlanetSettings> SETTINGS = new HashMap<>();

    public PlanetManager() {
        super(GSON, "planet_settings");
    }

    protected void apply(Map<ResourceLocation, JsonElement> pJsonMap, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        pJsonMap.forEach(((resourceLocation, jsonElement) -> SETTINGS.put(resourceLocation, PlanetSettings.fromGson(resourceLocation, jsonElement.getAsJsonObject()))));
    }

    public static void replaceSettings(Iterable<PlanetSettings> pSettings) {
        SETTINGS = new HashMap<>();
        pSettings.forEach(settings -> SETTINGS.put(settings.location(), settings));
    }

    public static Collection<PlanetSettings> getSettings() {
        return SETTINGS.values();
    }

    public static PlanetSettings getSettings(ResourceKey<Level> k){
        if (SETTINGS.containsKey(k.location())) return SETTINGS.get(k.location());
        return SETTINGS.get(Main.loc("default"));
    }

    public static PlanetSettings getSettings(Player p){
        if (SETTINGS.containsKey(p.level().dimension().location())) return SETTINGS.get(p.level().dimension().location());
        return SETTINGS.get(Main.loc("default"));
    }
}
