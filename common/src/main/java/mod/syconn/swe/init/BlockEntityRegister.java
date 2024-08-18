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

    public static final Supplier<BlockEntityType<CanisterFillerBlockEntity>> FILLER = register("filler", CanisterFillerBlockEntity::new, BlockRegister.CANISTER_FILLER);
    public static final Supplier<BlockEntityType<TankBE>> TANK = register("tank", createBEType(TankBE::new), BlockRegister.FLUID_TANK);
    public static final Supplier<BlockEntityType<DisperserBE>> DISPERSER = register("disperser", createBEType(DisperserBE::new), BlockRegister.OXYGEN_DISPERSER);
    public static final Supplier<BlockEntityType<CollectorBE>> COLLECTOR = register("collector", createBEType(CollectorBE::new), BlockRegister.OXYGEN_COLLECTOR);
    public static final Supplier<BlockEntityType<FluidPipeBE>> PIPE = register("pipe", createBEType(FluidPipeBE::new), BlockRegister.FLUID_PIPE);
    public static final Supplier<BlockEntityType<AirBlockEntity>> AIR = register("air", AirBlockEntity::new, BlockRegister.DISPERSED_OXYGEN);

    public static void init() {}

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String id, BiFunction<BlockPos, BlockState, T> function, Supplier<Block> blockSupplier) {
        return Services.REGISTRAR.registerBlockEntity(id, function, blockSupplier);
    }

    private static <T extends BlockEntity> BiFunction<BlockPos, BlockState, T> createBEType(BiFunction<BlockPos, BlockState, T> function) {
        return Services.REGISTRAR.createBEType(function);
    }
}
