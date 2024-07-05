package mod.syconn.swe.client.datagen;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import mod.syconn.swe.Main;
import mod.syconn.swe.blocks.CanisterFiller;
import mod.syconn.swe.util.data.PipeModule;

public class BlockModelGen extends BlockStateProvider {

    public BlockModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Main.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(ModInit.FLUID_PIPE.get()).forAllStates(state -> {
            PipeModule mod = new PipeModule(state);
            return ConfiguredModel.builder()
                    .modelFile(generated(mod.getModel()))
                    .rotationY(mod.getYRotation())
                    .uvLock(false)
                    .build();
        });
        getVariantBuilder(ModInit.CANISTER_FILLER.get()).forAllStates(state -> {
           return ConfiguredModel.builder()
                   .modelFile(generated(ForgeRegistries.BLOCKS.getKey(ModInit.CANISTER_FILLER.get()).getPath()))
                   .rotationY(state.getValue(CanisterFiller.FACING) == Direction.WEST || state.getValue(CanisterFiller.FACING) == Direction.EAST ? 90 : 0)
                   .uvLock(false)
                   .build();
        });
        simpleBlock(ModInit.FLUID_TANK.get(), generated("fluid_tank"));
        simpleBlock(ModInit.OXYGEN_COLLECTOR.get(), generated("oxygen_collector"));
        simpleBlock(ModInit.OXYGEN_DISPERSER.get(), generated("oxygen_disperser"));
        simpleBlock(ModInit.OXYGEN.get(), "minecraft:translucent");
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(ModInit.FLUID_PIPE.get()).getPath(), modLoc("block/fluid_pipe"));
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(ModInit.FLUID_TANK.get()).getPath(), modLoc("block/fluid_tank"));
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(ModInit.OXYGEN_COLLECTOR.get()).getPath(), modLoc("block/oxygen_collector"));
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(ModInit.CANISTER_FILLER.get()).getPath(), modLoc("block/canister_filler"));
        itemModels().withExistingParent(BuiltInRegistries.BLOCK.getKey(ModInit.OXYGEN_DISPERSER.get()).getPath(), modLoc("block/oxygen_disperser"));
    }

    private ModelFile generated(String loc) {
        return new ModelFile.UncheckedModelFile(modLoc("block/" + loc));
    }

    private void simpleBlock(Block block, String renderType) {
        simpleBlock(block, cubeAll(block, renderType));
    }

    private ModelFile cubeAll(Block block, String renderType) {
        return models().cubeAll(ForgeRegistries.BLOCKS.getKey(block).getPath(), blockTexture(block)).renderType(renderType);
    }

}
