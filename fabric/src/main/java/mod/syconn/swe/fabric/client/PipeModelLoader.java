package mod.syconn.swe.fabric.client;

import mod.syconn.swe.client.model.PipeBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class PipeModelLoader implements UnbakedModel {

    public Collection<ResourceLocation> getDependencies() {
        return List.of();
    }

    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
    }

    public @Nullable BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state, ResourceLocation location) {
        return new PipeBakedModel(false, 0.3, new String[] {"block/loader/fluid_pipe/connector", "block/loader/fluid_pipe/normal", "block/loader/fluid_pipe/none",
                "block/loader/fluid_pipe/end", "block/loader/fluid_pipe/corner", "block/loader/fluid_pipe/three", "block/loader/fluid_pipe/cross", "block/loader/fluid_pipe/side_block",
                "block/loader/fluid_pipe/side_import", "block/loader/fluid_pipe/side_export", "block/loader/fluid_pipe/side_both"}); // TODO Pull From GSON OBJECT FILE
    }
}
