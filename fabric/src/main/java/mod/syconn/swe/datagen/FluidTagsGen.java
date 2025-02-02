package mod.syconn.swe.datagen;

import mod.syconn.swe.core.ModFluids;
import mod.syconn.swe.core.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class FluidTagsGen extends FabricTagProvider.FluidTagProvider {

    public FluidTagsGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Fluids.OXYGEN).add(reverseLookup(ModFluids.O2_FLOWING.get())).add(reverseLookup(ModFluids.O2.get()));
    }
}
