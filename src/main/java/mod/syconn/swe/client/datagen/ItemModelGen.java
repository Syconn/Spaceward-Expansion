package mod.syconn.swe.client.datagen;

import mod.syconn.swe.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import mod.syconn.swe.Main;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModelGen extends ItemModelProvider {

    public ItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Main.MODID, existingFileHelper);
    }

    protected void registerModels() {
        singleTexture(Registration.SPACE_HELMET.get());
        singleTexture(Registration.SPACE_CHESTPLATE.get());
        singleTexture(Registration.SPACE_LEGGINGS.get());
        singleTexture(Registration.SPACE_BOOTS.get());
        singleTexture(Registration.WRENCH.get());
        singleTexture(Registration.DIAMOND_UPGRADE.get());
        singleTexture(Registration.GOLD_UPGRADE.get());
        singleTexture(Registration.IRON_UPGRADE.get());
        singleTexture(Registration.EMERALD_UPGRADE.get());
        singleTexture(Registration.NETHERITE_UPGRADE.get());

        createCanisterType(Registration.CANISTER.get());
        createCanisterType(Registration.AUTO_REFILL_CANISTER.get());
    }

    private ResourceLocation generated(){
        return ResourceLocation.withDefaultNamespace("item/generated");
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
            model.override().predicate(modLoc("stage"), i).model(generated("item/" + canister + i)).end();
        }
        getBuilder(canister + "full").parent(generate()).texture("layer1", modLoc("item/fluid_full")).texture("layer0", modLoc("item/" + canister));
        model.override().predicate(modLoc("stage"), 6.0F).model(generated("item/" + canister + "full"));
    }

    public ItemModelBuilder singleTexture(Item item) {
        return super.singleTexture(item.toString(), generated(), "layer0", modLoc("item/" + item));
    }
}
