package mod.syconn.swe.client.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import mod.syconn.swe.Main;

import java.util.concurrent.CompletableFuture;

public class BlockTagsGen extends BlockTagsProvider {

    public BlockTagsGen(PackOutput p_256596_, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256596_, p_256513_, Main.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModInit.FLUID_TANK.get(), ModInit.OXYGEN_DISPERSER.get(), ModInit.CANISTER_FILLER.get(), ModInit.OXYGEN_COLLECTOR.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModInit.FLUID_TANK.get(), ModInit.OXYGEN_DISPERSER.get(), ModInit.CANISTER_FILLER.get(), ModInit.OXYGEN_COLLECTOR.get());
        this.tag(ModTags.O2_PRODUCING).add(Blocks.GRASS, Blocks.FERN, Blocks.VINE, Blocks.GLOW_LICHEN, Blocks.SUNFLOWER, Blocks.LILAC, Blocks.ROSE_BUSH, Blocks.PEONY, Blocks.TALL_GRASS, Blocks.LARGE_FERN, Blocks.HANGING_ROOTS).addTags(BlockTags.LEAVES).addTag(BlockTags.CROPS);
    }
}
