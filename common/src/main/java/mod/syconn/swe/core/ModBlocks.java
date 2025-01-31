package mod.syconn.swe.core;

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swe.common.blocks.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

import static mod.syconn.swe.Constants.MOD;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD, Registries.BLOCK);

    public static final RegistrySupplier<Block> OXYGEN_COLLECTOR = registerBlockAndItem("oxygen_collector", () -> new OxygenCollectorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistrySupplier<Block> CANISTER_FILLER = registerBlockAndItem("canister_filler", () -> new CanisterFillerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistrySupplier<Block> OXYGEN_DISPERSER = registerBlockAndItem("oxygen_disperser", () -> new OxygenDisperserBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistrySupplier<Block> FLUID_TANK = registerBlockAndItem("fluid_tank", () -> new FluidTankBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final RegistrySupplier<Block> FLUID_PIPE = registerBlockAndItem("fluid_pipe", () -> new FluidPipeBlock(BlockBehaviour.Properties.copy(Blocks.GLASS_PANE)));
    public static final RegistrySupplier<Block> DISPERSED_OXYGEN = BLOCKS.register("dispersed_oxygen", () -> new DispersedAirBlock(BlockBehaviour.Properties.copy(Blocks.AIR).noCollission().noLootTable().air().isViewBlocking((state, level, pos) -> false)));
    public static final RegistrySupplier<LiquidBlock> O2_FLUID_BLOCK = BLOCKS.register("oxygen", () -> new ArchitecturyLiquidBlock(ModFluids.O2, BlockBehaviour.Properties.copy(Blocks.LAVA).lightLevel(blockstate -> 0)));

    private static <T extends Block> RegistrySupplier<T> registerBlockAndItem(String id, Supplier<T> block) {
        RegistrySupplier<T> registeredBlock = BLOCKS.register(id, block);
        ModItems.ITEMS.register(id, () -> new BlockItem(registeredBlock.get(), new Item.Properties()));
        return registeredBlock;
    }
}
