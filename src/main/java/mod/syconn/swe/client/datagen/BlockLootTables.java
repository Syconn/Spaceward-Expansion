package mod.syconn.swe.client.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.List;

public class BlockLootTables extends BlockLootSubProvider {

    public BlockLootTables() {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModInit.FLUID_TANK.get());
        dropSelf(ModInit.FLUID_PIPE.get());
        dropSelf(ModInit.OXYGEN_DISPERSER.get());
        dropSelf(ModInit.CANISTER_FILLER.get());
        dropSelf(ModInit.OXYGEN_COLLECTOR.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(ModInit.FLUID_TANK.get(), ModInit.FLUID_PIPE.get(), ModInit.OXYGEN_DISPERSER.get(), ModInit.CANISTER_FILLER.get(), ModInit.OXYGEN_COLLECTOR.get());
    }
}
