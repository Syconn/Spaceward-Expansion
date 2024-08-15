package mod.syconn.swe.init;

import mod.syconn.swe.blockentities.*;
import mod.syconn.swe.blockentities.FluidPipeBE;
import mod.syconn.swe.extra.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BlockEntityRegister {

    public static final Supplier<BlockEntityType<TankBE>> TANK = register("tank", createBEType(TankBE::new), blocks(BlockRegister.FLUID_TANK.get()));
    public static final Supplier<BlockEntityType<CanisterFillerBlockEntity>> FILLER = register("filler", CanisterFillerBlockEntity::new, blocks(BlockRegister.CANISTER_FILLER.get()));
    public static final Supplier<BlockEntityType<DisperserBE>> DISPERSER = register("disperser", createBEType(DisperserBE::new), blocks(BlockRegister.OXYGEN_DISPERSER.get()));
    public static final Supplier<BlockEntityType<CollectorBE>> COLLECTOR = register("collector", createBEType(CollectorBE::new), blocks(BlockRegister.OXYGEN_COLLECTOR.get()));
    public static final Supplier<BlockEntityType<FluidPipeBE>> PIPE = register("pipe", createBEType(FluidPipeBE::new), blocks(BlockRegister.FLUID_PIPE.get()));
    public static final Supplier<BlockEntityType<AirBlockEntity>> AIR = register("air", AirBlockEntity::new, blocks(BlockRegister.DISPERSED_OXYGEN.get()));

    public static void init() {}

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String id, BiFunction<BlockPos, BlockState, T> function, Supplier<Block[]> blockSupplier) {
        return Services.REGISTRAR.registerBlockEntity(id, function, blockSupplier);
    }

    private static <T extends BlockEntity> BiFunction<BlockPos, BlockState, T> createBEType(BiFunction<BlockPos, BlockState, T> function) {
        return Services.REGISTRAR.createBEType(function);
    }

    private static Supplier<Block[]> blocks(Block... blocks) {
        return () -> blocks;
    }
}
