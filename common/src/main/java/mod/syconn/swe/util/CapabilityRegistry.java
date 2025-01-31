package mod.syconn.swe.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mod.syconn.swe.common.blockentities.FluidHolderBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CapabilityRegistry {

    @ExpectPlatform
    public static void registerFluidItem() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends BlockEntity & FluidHolderBlock.IFluidHolderBlock> void registerFluidBlock(BlockEntityType<T> blockEntityType) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends BlockEntity & FluidHolderBlock.IFluidHolderBlock> void registerSidedFluidBlock(BlockEntityType<T> blockEntityType, Direction... not) {
        throw new AssertionError();
    }
}
