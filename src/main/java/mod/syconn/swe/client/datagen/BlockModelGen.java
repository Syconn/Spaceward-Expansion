package mod.syconn.swe.client.datagen;

import mod.syconn.swe.Main;
import mod.syconn.swe.Registration;
import mod.syconn.swe.blocks.CanisterFiller;
import mod.syconn.swe.util.data.PipeModule;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockModelGen extends BlockStateProvider {

    public BlockModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Main.MODID, existingFileHelper);
    }

    protected void registerStatesAndModels() {
        getVariantBuilder(Registration.FLUID_PIPE.get()).forAllStates(state -> {
            PipeModule mod = new PipeModule(state);
            return ConfiguredModel.builder()
                    .modelFile(generated(mod.getModel()))
                    .rotationY(mod.getYRotation())
                    .uvLock(false)
                    .build();
        });
        getVariantBuilder(Registration.CANISTER_FILLER.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(generated(BuiltInRegistries.BLOCK.getKey(Registration.CANISTER_FILLER.get()).getPath()))
                .rotationY(state.getValue(CanisterFiller.FACING) == Direction.WEST || state.getValue(CanisterFiller.FACING) == Direction.EAST ? 90 : 0)
                .uvLock(false)
                .build());
        simpleBlock(Registration.FLUID_TANK.get(), generated("fluid_tank"));
        simpleBlock(Registration.OXYGEN_COLLECTOR.get(), generated("oxygen_collector"));
        simpleBlock(Registration.OXYGEN_DISPERSER.get(), generated("oxygen_disperser"));
        simpleBlock(Registration.OXYGEN_DISPERSIBLE.get(), "minecraft:translucent");
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(Registration.FLUID_PIPE.get()).getPath(), modLoc("block/fluid_pipe"));
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(Registration.FLUID_TANK.get()).getPath(), modLoc("block/fluid_tank"));
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(Registration.OXYGEN_COLLECTOR.get()).getPath(), modLoc("block/oxygen_collector"));
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(Registration.CANISTER_FILLER.get()).getPath(), modLoc("block/canister_filler"));
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(Registration.OXYGEN_DISPERSER.get()).getPath(), modLoc("block/oxygen_disperser"));
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(Registration.OXYGEN_DISPERSIBLE.get()).getPath(), modLoc("block/oxygen_dispersible"));
    }

    private ModelFile generated(String loc) {
        return new ModelFile.UncheckedModelFile(modLoc("block/" + loc));
    }

    private void simpleBlock(Block block, String renderType) {
        simpleBlock(block, cubeAll(block, renderType));
    }

    private ModelFile cubeAll(Block block, String renderType) {
        return models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath(), blockTexture(block)).renderType(renderType);
    }
}