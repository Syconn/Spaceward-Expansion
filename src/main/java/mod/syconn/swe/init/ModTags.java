package mod.syconn.swe.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import mod.syconn.swe.Main;

public class ModTags {

    public static final TagKey<Item> GLASS = ItemTags.create(new ResourceLocation(Main.MODID, "glass"));
    public static final TagKey<Block> O2_PRODUCING = BlockTags.create(new ResourceLocation(Main.MODID, "o2_producing"));
}
