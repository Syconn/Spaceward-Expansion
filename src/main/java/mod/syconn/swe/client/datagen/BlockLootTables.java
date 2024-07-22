package mod.syconn.swe.client.datagen;

import mod.syconn.swe.Registration;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BlockLootTables extends BlockLootSubProvider {

    private static final Set<Item> EXPLOSION_RESISTANT = Collections.emptySet();

    public BlockLootTables(HolderLookup.Provider pRegistries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    protected void generate() {
        dropSelf(Registration.FLUID_TANK.get());
        dropSelf(Registration.OXYGEN_DISPERSER.get());
        dropSelf(Registration.CANISTER_FILLER.get());
        dropSelf(Registration.OXYGEN_COLLECTOR.get());
    }

    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(Holder::value).toList();
    }
}
