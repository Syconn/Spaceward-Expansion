package mod.syconn.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mod.syconn.api.client.loader.PipeModelLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class DataGenHelper {

    public static void registerItemCables(BlockStateProvider gen, Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        BlockModelBuilder model = gen.models().getBuilder(name)
                .parent(gen.models().getExistingFile(ResourceLocation.parse("cube"))).renderType("cutout")
                .customLoader((builder, helper) -> new PipeLoaderBuilder(PipeModelLoader.GENERATOR_LOADER, builder, helper, false, .3, new String[]{"block/loader/" + name + "/connector",
                        "block/loader/" + name + "/normal", "block/loader/" + name + "/none", "block/loader/" + name + "/end", "block/loader/" + name + "/corner", "block/loader/" + name + "/three", "block/loader/" + name + "/cross", "block/loader/" + name + "/side"}))
                .end();
        gen.simpleBlockWithItem(block, model);
    }

    private static class PipeLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

        private final boolean facade;
        private final double size;
        private final String[] textures;

        public PipeLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper, boolean facade, double size, String[] textures) {
            super(loader, parent, existingFileHelper, false);
            this.facade = facade;
            this.size = size;
            this.textures = textures;
        }

        public JsonObject toJson(JsonObject json) {
            JsonObject obj = super.toJson(json);
            obj.addProperty("facade", facade);
            obj.addProperty("size", size);
            obj.add("locations", new Gson().toJsonTree(textures));
            return obj;
        }
    }
}
