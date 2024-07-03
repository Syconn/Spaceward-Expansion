package mod.syconn.swe.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import mod.syconn.swe.Main;
import mod.syconn.swe.common.be.*;

import java.util.function.Supplier;

public class ModBlockEntity {

    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);

    public static final RegistryObject<BlockEntityType<PipeBlockEntity>> PIPE = register("pipe_be", PipeBlockEntity::new, () -> new Block[]{ModInit.FLUID_PIPE.get()});
    public static final RegistryObject<BlockEntityType<TankBlockEntity>> TANK = register("tank_be", TankBlockEntity::new, () -> new Block[]{ModInit.FLUID_TANK.get()});
    public static final RegistryObject<BlockEntityType<CanisterFillerBlockEntity>> FILLER = register("filler_be", CanisterFillerBlockEntity::new, () -> new Block[]{ModInit.CANISTER_FILLER.get()});
    public static final RegistryObject<BlockEntityType<DisperserBlockEntity>> DISPERSER = register("disperser_be", DisperserBlockEntity::new, () -> new Block[]{ModInit.OXYGEN_DISPERSER.get()});
    public static final RegistryObject<BlockEntityType<AirBlockEntity>> AIR = register("air_be", AirBlockEntity::new, () -> new Block[]{ModInit.OXYGEN.get()});
    public static final RegistryObject<BlockEntityType<CollectorBlockEntity>> COLLECTOR = register("collector_be", CollectorBlockEntity::new, () -> new Block[]{ModInit.OXYGEN_COLLECTOR.get()});

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block[]> validBlocksSupplier)
    {
        return REGISTER.register(name, () -> BlockEntityType.Builder.of(supplier, validBlocksSupplier.get()).build(null));
    }
}
