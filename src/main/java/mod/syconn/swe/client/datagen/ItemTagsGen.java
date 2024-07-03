package mod.syconn.swe.client.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import mod.syconn.swe.Main;
import mod.syconn.swe.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagsGen extends TagsProvider<Item> {

    public ItemTagsGen(PackOutput p_256596_, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256596_, Registries.ITEM, p_256513_, Main.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.GLASS).replace(false).addTags(Tags.Items.GLASS);
    }
}
