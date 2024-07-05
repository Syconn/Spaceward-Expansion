package mod.syconn.swe.client.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import mod.syconn.swe.Main;

public class ItemModelGen extends ItemModelProvider {

    public ItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Main.MODID, existingFileHelper);
    }

    protected void registerModels() {
        singleTexture(ModInit.SPACE_HELMET.get());
        singleTexture(ModInit.SPACE_CHESTPLATE.get());
        singleTexture(ModInit.SPACE_LEGGINGS.get());
        singleTexture(ModInit.SPACE_BOOTS.get());
        singleTexture(ModInit.WRENCH.get());
        singleTexture(ModInit.DIAMOND_UPGRADE.get());
        singleTexture(ModInit.GOLD_UPGRADE.get());
        singleTexture(ModInit.IRON_UPGRADE.get());
        singleTexture(ModInit.EMERALD_UPGRADE.get());
        singleTexture(ModInit.NETHERITE_UPGRADE.get());

        createCanisterType(ModInit.CANISTER.get());
        createCanisterType(ModInit.AUTO_REFILL_CANISTER.get());
    }

    private ResourceLocation generated(){
        return new ResourceLocation("item/generated");
    }

    private ModelFile generated(String loc) {
        return new ModelFile.UncheckedModelFile(modLoc(loc));
    }

    private ModelFile.UncheckedModelFile generate(){
        return new ModelFile.UncheckedModelFile("item/generated");
    }

    private void createCanisterType(Item canister) {
        ItemModelBuilder model = getBuilder(canister.toString()).parent(generate()).texture("layer0", modLoc("item/" + canister));
        for (int i = 1; i <= 5; i++){
            getBuilder(canister.toString() + i).parent(generate()).texture("layer1", modLoc("item/fluid_stage_" + (i + 1))).texture("layer0", modLoc("item/" + canister));
            model.override().predicate(new ResourceLocation(Main.MODID, "stage"), i).model(generated("item/" + canister + i)).end();
        }
        getBuilder(canister + "full").parent(generate()).texture("layer1", modLoc("item/fluid_full")).texture("layer0", modLoc("item/" + canister));
        model.override().predicate(new ResourceLocation(Main.MODID, "stage"), 6.0F).model(generated("item/" + canister + "full"));
    }

    public ItemModelBuilder singleTexture(Item item) {
        return super.singleTexture(item.toString(), generated(), "layer0", modLoc("item/" + item));
    }
}
