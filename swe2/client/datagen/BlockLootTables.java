package mod.syconn.swe2.client.datagen;

import mod.syconn.swe2.Registration;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Collections;

public class BlockLootTables extends BlockLootSubProvider {

    public BlockLootTables(HolderLookup.Provider pRegistries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    protected void generate() {
        dropSelf(Registration.FLUID_TANK.get());
        dropSelf(Registration.OXYGEN_DISPERSER.get());
        dropSelf(Registration.CANISTER_FILLER.get());
        dropSelf(Registration.OXYGEN_COLLECTOR.get());
        dropWhenSilkTouch(Registration.FLUID_PIPE.get());
    }

    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(Holder::value).toList();
    }
}
