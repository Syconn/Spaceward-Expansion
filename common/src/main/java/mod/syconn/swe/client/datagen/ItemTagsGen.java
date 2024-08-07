package mod.syconn.swe.client.datagen;

import mod.syconn.swe.Main;
import mod.syconn.swe.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagsGen extends ItemTagsProvider {

    public ItemTagsGen(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, Main.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Registration.CANISTERS).add(Registration.CANISTER.get(), Registration.AUTO_REFILL_CANISTER.get());
        this.tag(ItemTags.DYEABLE).replace(false).add(Registration.PARACHUTE.get());
    }
}
