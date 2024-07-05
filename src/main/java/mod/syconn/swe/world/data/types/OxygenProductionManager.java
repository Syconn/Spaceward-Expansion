package mod.syconn.swe.world.data.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class OxygenProductionManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static Map<Block, Double> BLOCKS = new HashMap<>();
    private static Map<TagKey<Block>, Double> TAGS = new HashMap<>();

    public OxygenProductionManager() {
        super(GSON, "oxygen_production");
    }

    protected void apply(Map<ResourceLocation, JsonElement> p_10793_, ResourceManager p_10794_, ProfilerFiller p_10795_) {
        p_10793_.forEach(((resourceLocation, jsonElement) -> {
            JsonObject json = jsonElement.getAsJsonObject();
            if (json.has("tag")) {
                TAGS.put(TagKey.create(Registries.BLOCK, new ResourceLocation(json.get("tag").getAsString())), json.get("value").getAsDouble());
            } else {
                BLOCKS.put(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(json.get("block").getAsString())), json.get("value").getAsDouble());
            }
        }));
    }

    public static double getValue(BlockState block) {
        if (BLOCKS.containsKey(block.getBlock())) return BLOCKS.get(block.getBlock());
        for (Map.Entry<TagKey<Block>, Double> entry : TAGS.entrySet()) {
            if (block.is(entry.getKey())) return entry.getValue();
        }
        return block.is(ModTags.O2_PRODUCING) ? 1 : 0;
    }
}
