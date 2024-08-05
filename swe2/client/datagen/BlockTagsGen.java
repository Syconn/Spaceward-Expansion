package mod.syconn.swe2.client.datagen;

import mod.syconn.swe2.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import mod.syconn.swe2.Main;

import java.util.concurrent.CompletableFuture;

public class BlockTagsGen extends BlockTagsProvider {

    public BlockTagsGen(PackOutput p_256596_, CompletableFuture<HolderLookup.Provider> p_256513_, ExistingFileHelper existingFileHelper) {
        super(p_256596_, p_256513_, Main.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(Registration.FLUID_PIPE.get(), Registration.FLUID_TANK.get(), Registration.OXYGEN_DISPERSER.get(), Registration.CANISTER_FILLER.get(), Registration.OXYGEN_COLLECTOR.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.FLUID_PIPE.get(), Registration.FLUID_TANK.get(), Registration.OXYGEN_DISPERSER.get(), Registration.CANISTER_FILLER.get(), Registration.OXYGEN_COLLECTOR.get());
        this.tag(Registration.O2_PRODUCING).add(Blocks.GRASS_BLOCK, Blocks.FERN, Blocks.VINE, Blocks.GLOW_LICHEN, Blocks.SUNFLOWER, Blocks.LILAC, Blocks.ROSE_BUSH, Blocks.PEONY, Blocks.TALL_GRASS, Blocks.LARGE_FERN, Blocks.HANGING_ROOTS).addTags(BlockTags.LEAVES).addTag(BlockTags.CROPS);
        this.tag(Registration.INFINBURN_MOON).replace(false);
    }
}
