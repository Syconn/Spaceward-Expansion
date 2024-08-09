package mod.syconn.swe.init;

import mod.syconn.swe.api.blockEntity.BaseFluidPipeBE;
import mod.syconn.swe.blockentities.*;
import mod.syconn.swe.platform.Services;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockEntityRegister {

    public static final Supplier<BlockEntityType<TankBE>> TANK = register("tank", () -> BlockEntityType.Builder.of(TankBE::new, BlockRegister.FLUID_TANK.get()).build(null));
    public static final Supplier<BlockEntityType<CanisterFillerBlockEntity>> FILLER = register("filler", () -> BlockEntityType.Builder.of(CanisterFillerBlockEntity::new, BlockRegister.CANISTER_FILLER.get()).build(null));
    public static final Supplier<BlockEntityType<DisperserBE>> DISPERSER = register("disperser", () -> BlockEntityType.Builder.of(DisperserBE::new, BlockRegister.OXYGEN_DISPERSER.get()).build(null));
    public static final Supplier<BlockEntityType<AirBlockEntity>> AIR = register("air", () -> BlockEntityType.Builder.of(AirBlockEntity::new, BlockRegister.OXYGEN_DISPERSIBLE.get()).build(null));
    public static final Supplier<BlockEntityType<CollectorBE>> COLLECTOR = register("collector", () -> BlockEntityType.Builder.of(CollectorBE::new, BlockRegister.OXYGEN_COLLECTOR.get()).build(null));
    public static final Supplier<BlockEntityType<BaseFluidPipeBE>> PIPE = register("pipe", () -> BlockEntityType.Builder.of(BaseFluidPipeBE::new, BlockRegister.FLUID_PIPE.get()).build(null));

    public static void init() {}

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> blockEntityType) {
        return Services.REGISTRAR.registerBlockEntity(id, blockEntityType);
    }
}
