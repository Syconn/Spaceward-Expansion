package mod.syconn.swe.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.swe.common.blockentities.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static mod.syconn.swe.Constants.MOD;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(MOD, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<CanisterFillerBlockEntity>> FILLER = BLOCK_ENTITIES.register("filler", () -> BlockEntityType.Builder.of(CanisterFillerBlockEntity::new, ModBlocks.CANISTER_FILLER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<TankBE>> TANK = BLOCK_ENTITIES.register("tank", () -> BlockEntityType.Builder.of(TankBE::new, ModBlocks.FLUID_TANK.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<DisperserBE>> DISPERSER = BLOCK_ENTITIES.register("disperser", () -> BlockEntityType.Builder.of(DisperserBE::new, ModBlocks.OXYGEN_DISPERSER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CollectorBE>> COLLECTOR = BLOCK_ENTITIES.register("collector", () -> BlockEntityType.Builder.of(CollectorBE::new, ModBlocks.OXYGEN_COLLECTOR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<FluidPipeBE>> PIPE = BLOCK_ENTITIES.register("pipe", () -> BlockEntityType.Builder.of(FluidPipeBE::new, ModBlocks.FLUID_PIPE.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<AirBlockEntity>> AIR = BLOCK_ENTITIES.register("air", () -> BlockEntityType.Builder.of(AirBlockEntity::new, ModBlocks.DISPERSED_OXYGEN.get()).build(null));
}
