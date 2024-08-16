package mod.syconn.swe.model.loader;

import mod.syconn.swe.model.PipeBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class PipeModelLoader implements UnbakedModel {

    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptySet();
    }

    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {}

    public @Nullable BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state) {
        return new PipeBakedModel(false, 0.3, new String[] {"block/loader/fluid_pipe/connector", "block/loader/fluid_pipe/normal", "block/loader/fluid_pipe/none",
                "block/loader/fluid_pipe/end", "block/loader/fluid_pipe/corner", "block/loader/fluid_pipe/three", "block/loader/fluid_pipe/cross", "block/loader/fluid_pipe/side_block",
                "block/loader/fluid_pipe/side_import", "block/loader/fluid_pipe/side_export", "block/loader/fluid_pipe/side_both"}); // TODO MAKE IT WORK LIKE OTHERS
    }
}
