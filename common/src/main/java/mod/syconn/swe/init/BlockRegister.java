package mod.syconn.swe.init;

import com.mojang.serialization.MapCodec;
import mod.syconn.swe.api.blocks.BaseFluidPipe;
import mod.syconn.swe.blocks.*;
import mod.syconn.swe.fluids.LiquidBlock;
import mod.syconn.swe.platform.Services;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.state.BlockBehaviour.simpleCodec;

public class BlockRegister {

    public static final Supplier<OxygenCollector> OXYGEN_COLLECTOR = registerBlockAndItem("oxygen_collector", () -> new OxygenCollector(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final Supplier<DispersibleAirBlock> OXYGEN_DISPERSIBLE = registerBlockAndItem("oxygen_dispersible", () -> new DispersibleAirBlock(Blocks.AIR.properties().noCollission().noLootTable().air().isViewBlocking((state, level, pos) -> false)));
    public static final Supplier<CanisterFiller> CANISTER_FILLER = registerBlockAndItem("canister_filler", () -> new CanisterFiller(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final Supplier<OxygenDisperser> OXYGEN_DISPERSER = registerBlockAndItem("oxygen_disperser", () -> new OxygenDisperser(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final Supplier<Block> FLUID_TANK = registerBlockAndItem("fluid_tank", () -> new FluidTank(Blocks.IRON_BLOCK.properties().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final Supplier<BaseFluidPipe> FLUID_PIPE = registerBlockAndItem("fluid_pipe", () -> new BaseFluidPipe(Blocks.GLASS_PANE.properties()));
    public static final Supplier<LiquidBlock> O2 = Services.REGISTRAR.registerBlock("oxygen", () -> new LiquidBlock(FluidRegister.O2.get(), Blocks.LAVA.properties().lightLevel(blockstate -> 0)));

    public static final Supplier<MapCodec<CanisterFiller>> CANISTER_FILLER_CODEC = registerCodec("canister_filler", () -> simpleCodec(CanisterFiller::new));
    public static final Supplier<MapCodec<FluidTank>> FLUID_TANK_CODEC = registerCodec("fluid_tank", () -> simpleCodec(FluidTank::new));
    public static final Supplier<MapCodec<OxygenCollector>> OXYGEN_COLLECTOR_CODEC = registerCodec("oxygen_collector", () -> simpleCodec(OxygenCollector::new));
    public static final Supplier<MapCodec<OxygenDisperser>> OXYGEN_DISPERSER_CODEC = registerCodec("oxygen_disperser", () -> simpleCodec(OxygenDisperser::new));
    public static final Supplier<MapCodec<DispersibleAirBlock>> OXYGEN_CODEC = registerCodec("oxygen_dispersible", () -> simpleCodec(DispersibleAirBlock::new));
    public static final Supplier<MapCodec<BaseFluidPipe>> FlUID_PIPE_CODEC = registerCodec("fluid_pipe", () -> simpleCodec(BaseFluidPipe::new));

    public static void init() {}

    private static <T extends Block> Supplier<T> registerBlockAndItem(String id, Supplier<T> block) {
        Supplier<T> registeredBlock = Services.REGISTRAR.registerBlock(id, block);
        Services.REGISTRAR.registerItem(id, () -> new BlockItem(registeredBlock.get(), new Item.Properties()));
        return registeredBlock;
    }

//    private static <BLOCK extends Block> DeferredBlock<BLOCK> registerBlockAndItem(String id, Supplier<BLOCK> blockSupplier) { TODO OLD CODE TEST AND REMOVE
//        return registerBlockAndItem(id, blockSupplier, block1 -> new BlockItem(block1, new Item.Properties()));
//    }
//
//    private static <BLOCK extends Block, ITEM extends BlockItem> DeferredBlock<BLOCK> registerBlockAndItem(String name, Supplier<BLOCK> blockFactory, Function<? super BLOCK,ITEM> itemFactory) {
//        DeferredBlock<BLOCK> block = BLOCKS.register(name, blockFactory);
//        ITEMS.register(name, () -> itemFactory.apply(block.get()));
//        return block;
//    }

    private static <T extends Block> Supplier<MapCodec<T>> registerCodec(String id, Supplier<MapCodec<T>> blockCodec) {
        return Services.REGISTRAR.registerBlockCodec(id, blockCodec);
    }
}
