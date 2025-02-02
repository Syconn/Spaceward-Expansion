package mod.syconn.swe.core;

import mod.syconn.swe.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> CANISTERS = TagKey.create(Registries.ITEM, Constants.withId("canisters"));
        public static final TagKey<Item> GLASS_BLOCKS = TagKey.create(Registries.ITEM, Constants.withId("glass_block"));
        public static final TagKey<Item> GLASS_PANES = TagKey.create(Registries.ITEM, Constants.withId("glass_pane_block"));
    }

    public static class Blocks {
        public static final TagKey<Block> O2_PRODUCING = TagKey.create(Registries.BLOCK, Constants.withId("o2_producing"));
        public static final TagKey<Block> PERMABURN_MOON = TagKey.create(Registries.BLOCK, Constants.withId("infinburn_moon"));
    }

    public static class Fluids {
        public static final TagKey<Fluid> OXYGEN = TagKey.create(Registries.FLUID, Constants.withId("oxygen"));
    }

    public static class Planets {
        public static final ResourceKey<Level> MOON = ResourceKey.create(Registries.DIMENSION, Constants.withId("moon"));

    }
}
