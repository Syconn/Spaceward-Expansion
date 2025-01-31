package mod.syconn.swe.common.blockentities;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class InteractableFluidHolderBlock extends FluidHolderBlock {

    // TODO
    //  - Since not all Capabilities | Fluid Variants are FluidHoldersBlocks/Items then we need to wrap them in custom class if not ours to get and use them - probably works better with blocks
    //  - Good time for Direction Based controls maybe

    protected Map<Direction, Interaction> interactions = new HashMap<>() {{
        put(Direction.NORTH, Interaction.NONE);
        put(Direction.SOUTH, Interaction.NONE);
        put(Direction.EAST, Interaction.NONE);
        put(Direction.WEST, Interaction.NONE);
        put(Direction.DOWN, Interaction.NONE);
        put(Direction.UP, Interaction.NONE);
    }};

    private final int speed;

    public InteractableFluidHolderBlock(int speed) {
        this.speed = speed;
    }

    public Interaction getSideInteraction(Direction side) {
        return interactions.get(side);
    }

    public void setSideInteraction(Direction side, Interaction interaction) {
        interactions.put(side, interaction);
    }

    public void handlePush(Level level, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            if (getSideInteraction(direction).isPush() && hasHolder(level, blockPos.relative(direction), direction.getOpposite())) {
                FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, blockPos.relative(direction), direction.getOpposite());
                if (blockHandler.isFluidValid(getFluidHolder())) {
                    int fill = blockHandler.fill(getFluidHolder().copyWith(speed), FluidAction.SIMULATE);
                    blockHandler.fill(drain(Math.min(speed, fill), FluidAction.EXECUTE), FluidAction.EXECUTE);
                }
            }
        }
    }

    public void handlePull(Level level, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            if (getSideInteraction(direction).isPull() && Services.FLUID_HANDLER.has(level, blockPos.relative(direction), direction.getOpposite())) {
                FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, blockPos.relative(direction), direction.getOpposite());
                if (!blockHandler.getFluidHolder().isEmpty()) {
                    int fill = fill(getFluidHolder().copyWith(speed), FluidAction.SIMULATE);
                    fill(blockHandler.drain(Math.min(speed, fill), FluidAction.EXECUTE), FluidAction.EXECUTE);
                }
            }
        }
    }

    @ExpectPlatform
    public static InteractableFluidHolderBlock create(long capacity) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static InteractableFluidHolderBlock create(long capacity, Consumer<FluidHolderBlock> onChange) {
        throw new AssertionError();
    }

    public enum Interaction {
        PUSH, PULL, BOTH, NONE;

        boolean isPull() { return this == PULL || this == BOTH; }
        boolean isPush() { return this == PUSH || this == BOTH; }
    }

    public interface IInteractableFluidHolderBlock {
        InteractableFluidHolderBlock getFluidHolder();
    }
}
