package mod.syconn.api.blockEntity;

import mod.syconn.api.blocks.AbstractPipeBlock;
import mod.syconn.api.blocks.BaseFluidPipe;
import mod.syconn.api.util.PipeConnectionTypes;
import mod.syconn.api.world.data.savedData.PipeNetworks;
import mod.syconn.swe.Registration;
import mod.syconn.swe.blockentities.TankBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseFluidPipeBE extends AbstractPipeBE {

    public BaseFluidPipeBE(BlockPos pos, BlockState state) {
        super(Registration.PIPE.get(), pos, state);
    }

    public boolean canConnectToPipe(Level level, BlockPos pos, Direction conDir) {
        return level.getBlockState(pos.relative(conDir)).getBlock() instanceof BaseFluidPipe;
    }

    public boolean hasMenu() {
        for (Direction direction : Direction.values()) if (getConnectionType(direction).isInteractionPoint()) return true;
        return false;
    }

    public PipeConnectionTypes getConnectionType(Direction direction) {
        BlockState state = level.getBlockState(worldPosition);
        return switch (direction) {
            case DOWN -> state.getValue(AbstractPipeBlock.DOWN);
            case UP -> state.getValue(AbstractPipeBlock.UP);
            case NORTH -> state.getValue(AbstractPipeBlock.NORTH);
            case SOUTH -> state.getValue(AbstractPipeBlock.SOUTH);
            case WEST -> state.getValue(AbstractPipeBlock.WEST);
            case EAST -> state.getValue(AbstractPipeBlock.EAST);
        };
    }

    public void setConnectionType(Direction side, PipeConnectionTypes type) {
        BlockState state = level.getBlockState(worldPosition);
        switch (side) {
            case DOWN -> level.setBlock(worldPosition, state.setValue(AbstractPipeBlock.DOWN, type), 2);
            case UP -> level.setBlock(worldPosition, state.setValue(AbstractPipeBlock.UP, type), 2);
            case NORTH -> level.setBlock(worldPosition, state.setValue(AbstractPipeBlock.NORTH, type), 2);
            case SOUTH -> level.setBlock(worldPosition, state.setValue(AbstractPipeBlock.SOUTH, type), 2);
            case WEST -> level.setBlock(worldPosition, state.setValue(AbstractPipeBlock.WEST, type), 2);
            case EAST -> level.setBlock(worldPosition, state.setValue(AbstractPipeBlock.EAST, type), 2);
        };
    }
}
