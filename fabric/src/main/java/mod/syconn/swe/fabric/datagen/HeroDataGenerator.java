package mod.syconn.swe.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class HeroDataGenerator implements DataGeneratorEntrypoint {

    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(LangGen::new);
        pack.addProvider(RecipeGen::new);
        BlockTagsGen blockTags = pack.addProvider(BlockTagsGen::new);
        pack.addProvider((output, lookup) -> new ItemTagGen(output, lookup, blockTags));
        pack.addProvider(ModelGen::new);
        pack.addProvider(FluidTagsGen::new);
    }

//    public void buildRegistry(RegistrySetBuilder registryBuilder) {
//        DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
//        registryBuilder.add(Registries.DAMAGE_TYPE, DamageTypeGen::bootstrapDamageTypes);
//    }
}
