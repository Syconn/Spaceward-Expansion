package mod.syconn.swe.datagen;

import mod.syconn.swe.core.ModItems;
import mod.syconn.swe.core.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGen extends FabricTagProvider.ItemTagProvider {

    public ItemTagGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable BlockTagProvider blockTagProvider) {
        super(output, completableFuture, blockTagProvider);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Items.CANISTERS).add(reverseLookup(ModItems.CANISTER.get())).add(reverseLookup(ModItems.AUTO_REFILL_CANISTER.get()));
        this.tag(ModTags.Items.GLASS_BLOCKS).add(reverseLookup(Items.GLASS)).add(reverseLookup(Items.BLACK_STAINED_GLASS)).add(reverseLookup(Items.GREEN_STAINED_GLASS))
                .add(reverseLookup(Items.WHITE_STAINED_GLASS)).add(reverseLookup(Items.ORANGE_STAINED_GLASS)).add(reverseLookup(Items.MAGENTA_STAINED_GLASS)).add(reverseLookup(Items.LIGHT_BLUE_STAINED_GLASS))
                .add(reverseLookup(Items.YELLOW_STAINED_GLASS)).add(reverseLookup(Items.LIME_STAINED_GLASS)).add(reverseLookup(Items.PINK_STAINED_GLASS)).add(reverseLookup(Items.GRAY_STAINED_GLASS))
                .add(reverseLookup(Items.LIGHT_GRAY_STAINED_GLASS)).add(reverseLookup(Items.CYAN_STAINED_GLASS)).add(reverseLookup(Items.PURPLE_STAINED_GLASS)).add(reverseLookup(Items.BLUE_STAINED_GLASS))
                .add(reverseLookup(Items.BROWN_STAINED_GLASS)).add(reverseLookup(Items.RED_STAINED_GLASS));
        this.tag(ModTags.Items.GLASS_PANES).add(reverseLookup(Items.GLASS)).add(reverseLookup(Items.BLACK_STAINED_GLASS_PANE)).add(reverseLookup(Items.GREEN_STAINED_GLASS_PANE))
                .add(reverseLookup(Items.WHITE_STAINED_GLASS_PANE)).add(reverseLookup(Items.ORANGE_STAINED_GLASS_PANE)).add(reverseLookup(Items.MAGENTA_STAINED_GLASS_PANE)).add(reverseLookup(Items.LIGHT_BLUE_STAINED_GLASS_PANE))
                .add(reverseLookup(Items.YELLOW_STAINED_GLASS_PANE)).add(reverseLookup(Items.LIME_STAINED_GLASS_PANE)).add(reverseLookup(Items.PINK_STAINED_GLASS_PANE)).add(reverseLookup(Items.GRAY_STAINED_GLASS_PANE))
                .add(reverseLookup(Items.LIGHT_GRAY_STAINED_GLASS_PANE)).add(reverseLookup(Items.CYAN_STAINED_GLASS_PANE)).add(reverseLookup(Items.PURPLE_STAINED_GLASS_PANE)).add(reverseLookup(Items.BLUE_STAINED_GLASS_PANE))
                .add(reverseLookup(Items.BROWN_STAINED_GLASS_PANE)).add(reverseLookup(Items.RED_STAINED_GLASS_PANE));
    }
}
