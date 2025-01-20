package mod.syconn.swe.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swe.common.blocks.*;
import mod.syconn.swe.common.blocks.fluids.FluidBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

import static mod.syconn.swe.Constants.MOD;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD, Registries.BLOCK);

    public static final RegistrySupplier<Block> OXYGEN_COLLECTOR = registerBlockAndItem("oxygen_collector", () -> new OxygenCollector(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistrySupplier<Block> CANISTER_FILLER = registerBlockAndItem("canister_filler", () -> new CanisterFiller(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistrySupplier<Block> OXYGEN_DISPERSER = registerBlockAndItem("oxygen_disperser", () -> new OxygenDisperser(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistrySupplier<Block> FLUID_TANK = registerBlockAndItem("fluid_tank", () -> new FluidTank(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final RegistrySupplier<Block> FLUID_PIPE = registerBlockAndItem("fluid_pipe", () -> new FluidPipe(Blocks.GLASS_PANE));
    public static final RegistrySupplier<Block> DISPERSED_OXYGEN = BLOCKS.register("dispersed_oxygen", () -> new DispersedAirBlock(Blocks.AIR.properties().noCollission().noLootTable().air().isViewBlocking((state, level, pos) -> false)));
    public static final RegistrySupplier<Block> O2 = BLOCKS.register("oxygen", () -> new FluidBlock(FluidRegister.O2.get(), Blocks.LAVA.properties().lightLevel(blockstate -> 0)));

    private static <T extends Block> Supplier<T> registerBlockAndItem(String id, Supplier<T> block) {
        RegistrySupplier<T> registeredBlock = BLOCKS.register(id, block);
        ModItems.ITEMS.register(id, () -> new BlockItem(registeredBlock.get(), new Item.Properties()));
        return registeredBlock;
    }
}
