package mod.syconn.swe.datagen;

import mod.syconn.swe.registry.ModBlocks;
import mod.syconn.swe.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class ModelGen extends FabricModelProvider {

    public ModelGen(FabricDataOutput output) {
        super(output);
    }

    public void generateBlockStateModels(BlockModelGenerators generator) {
        generator.createTrivialCube(ModBlocks.FLUID_TANK.get());
        generator.createTrivialCube(ModBlocks.OXYGEN_COLLECTOR.get());
        generator.createTrivialCube(ModBlocks.OXYGEN_DISPERSER.get());
        generator.createTrivialCube(ModBlocks.DISPERSED_OXYGEN.get());
        generator.createSimpleFlatItemModel(ModBlocks.FLUID_TANK.get());
        generator.createSimpleFlatItemModel(ModBlocks.OXYGEN_COLLECTOR.get());
        generator.createSimpleFlatItemModel(ModBlocks.CANISTER_FILLER.get());
        generator.createSimpleFlatItemModel(ModBlocks.OXYGEN_DISPERSER.get());
        generator.createSimpleFlatItemModel(ModBlocks.DISPERSED_OXYGEN.get());
        generator.createRotatedVariantBlock(ModBlocks.CANISTER_FILLER.get());
        // TODO NO FLUID PIPE CODE
    }

    public void generateItemModels(ItemModelGenerators generator) {
        generator.generateFlatItem(ModItems.SPACE_HELMET.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.SPACE_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.SPACE_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.SPACE_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.DIAMOND_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.GOLD_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.IRON_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.EMERALD_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(ModItems.NETHERITE_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        // TODO NO CANISTER OR PARACHUTE Code
    }
}
