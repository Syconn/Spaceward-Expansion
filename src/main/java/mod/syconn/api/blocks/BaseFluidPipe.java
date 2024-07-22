package mod.syconn.api.blocks;

import com.mojang.serialization.MapCodec;
import mod.syconn.api.blockEntity.AbstractPipeBE;
import mod.syconn.api.blockEntity.BaseFluidPipeBE;
import mod.syconn.api.util.PipeConnectionTypes;
import mod.syconn.swe.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BaseFluidPipe extends AbstractPipeBlock {

    public BaseFluidPipe(Properties properties) {
        super(properties);
    }

    protected PipeConnectionTypes getConnectorType(BlockGetter level, BlockPos thisPos, BlockPos connectionPos, Direction facing) {
        return level.getBlockEntity(connectionPos) instanceof AbstractPipeBE ? PipeConnectionTypes.CABLE : PipeConnectionTypes.NONE;
    }

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return Registration.FLUID_TANK_CODEC.value();
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BaseFluidPipeBE(pPos, pState);
    }
}
