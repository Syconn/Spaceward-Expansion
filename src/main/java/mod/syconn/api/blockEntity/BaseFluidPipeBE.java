package mod.syconn.api.blockEntity;

import mod.syconn.api.blocks.BaseFluidPipe;
import mod.syconn.swe.Registration;
import mod.syconn.swe.blockentities.TankBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BaseFluidPipeBE extends AbstractPipeBE {

    public BaseFluidPipeBE(BlockPos pos, BlockState state) {
        super(Registration.PIPE.get(), pos, state);
    }

    public boolean canConnectToPipe(Level level, BlockPos pos, Direction conDir) {
        return level.getBlockState(pos.relative(conDir)).getBlock() instanceof BaseFluidPipe;
    }
}
