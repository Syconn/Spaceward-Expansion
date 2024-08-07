package mod.syconn.swe.world.dimensions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.syconn.swe.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OxygenProductionManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<Block, Double> BLOCKS = new HashMap<>();
    private static final Map<TagKey<Block>, Double> TAGS = new HashMap<>();

    public OxygenProductionManager() {
        super(GSON, "oxygen_production");
    }

    protected void apply(Map<ResourceLocation, JsonElement> p_10793_, ResourceManager p_10794_, ProfilerFiller p_10795_) {
        p_10793_.forEach(((resourceLocation, jsonElement) -> {
            JsonObject json = jsonElement.getAsJsonObject();
            if (json.has("tag")) TAGS.put(TagKey.create(Registries.BLOCK, Objects.requireNonNull(ResourceLocation.tryParse(json.get("tag").getAsString()))), json.get("value").getAsDouble());
            else BLOCKS.put(BuiltInRegistries.BLOCK.get(Objects.requireNonNull(ResourceLocation.tryParse(json.get("block").getAsString()))), json.get("value").getAsDouble());
        }));
    }

    public static double getValue(BlockState block) {
        if (BLOCKS.containsKey(block.getBlock())) return BLOCKS.get(block.getBlock());
        for (Map.Entry<TagKey<Block>, Double> entry : TAGS.entrySet()) {
            if (block.is(entry.getKey())) return entry.getValue();
        }
        return block.is(Registration.O2_PRODUCING) ? 1 : 0;
    }
}
