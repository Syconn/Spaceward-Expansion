package mod.syconn.swe.init;

import mod.syconn.swe.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class CommonTags {

    public static final ResourceKey<Level> MOON_KEY = ResourceKey.create(Registries.DIMENSION, Constants.MOON);

    public static final TagKey<Item> CANISTERS = TagKey.create(Registries.ITEM, Constants.loc("canisters"));

    public static final TagKey<Block> O2_PRODUCING = TagKey.create(Registries.BLOCK, Constants.loc("o2_producing"));
    public static final TagKey<Block> INFINBURN_MOON = TagKey.create(Registries.BLOCK, Constants.loc("infinburn_moon"));

    public static final TagKey<Fluid> OXYGEN = TagKey.create(Registries.FLUID, Constants.loc("oxygen"));
}
