package mod.syconn.swe.client.datagen;

import mod.syconn.swe.Main;
import mod.syconn.swe.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class FluidTagsGen extends FluidTagsProvider {

    public FluidTagsGen(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, Main.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(Registration.OXYGEN).add(Registration.O2_FLOWING.get()).add(Registration.O2.get());
    }
}
