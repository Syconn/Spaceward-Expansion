package mod.syconn.swe.datagen;

import mod.syconn.swe.registry.ModBlocks;
import mod.syconn.swe.registry.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagsGen extends FabricTagProvider.BlockTagProvider {

    public BlockTagsGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.addAll(BlockTags.NEEDS_IRON_TOOL, ModBlocks.FLUID_PIPE.get(), ModBlocks.FLUID_TANK.get(), ModBlocks.OXYGEN_DISPERSER.get(), ModBlocks.CANISTER_FILLER.get(), ModBlocks.OXYGEN_COLLECTOR.get());
        this.addAll(BlockTags.MINEABLE_WITH_PICKAXE, ModBlocks.FLUID_PIPE.get(), ModBlocks.FLUID_TANK.get(), ModBlocks.OXYGEN_DISPERSER.get(), ModBlocks.CANISTER_FILLER.get(), ModBlocks.OXYGEN_COLLECTOR.get());
        this.addAll(ModTags.Blocks.O2_PRODUCING, Blocks.GRASS_BLOCK, Blocks.FERN, Blocks.VINE, Blocks.GLOW_LICHEN, Blocks.SUNFLOWER, Blocks.LILAC, Blocks.ROSE_BUSH, Blocks.PEONY, Blocks.TALL_GRASS, Blocks.LARGE_FERN, Blocks.HANGING_ROOTS).addTag(BlockTags.LEAVES).addTag(BlockTags.CROPS);
        this.tag(ModTags.Blocks.PERMABURN_MOON);
    }

    private TagsProvider.TagAppender<Block> addAll(TagKey<Block> tagKey, Block... blocks) {
        var tag = this.tag(tagKey);
        for (Block block : blocks) tag.add(reverseLookup(block));
        return tag;
    }
}
