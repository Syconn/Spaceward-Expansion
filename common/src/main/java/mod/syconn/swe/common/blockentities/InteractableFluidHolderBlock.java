package mod.syconn.swe.common.blockentities;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class InteractableFluidHolderBlock extends FluidHolderBlock {

    protected Map<Direction, Interaction> interactions = new HashMap<>() {{
        put(Direction.NORTH, Interaction.NONE);
        put(Direction.SOUTH, Interaction.NONE);
        put(Direction.EAST, Interaction.NONE);
        put(Direction.WEST, Interaction.NONE);
        put(Direction.DOWN, Interaction.NONE);
        put(Direction.UP, Interaction.NONE);
    }};

    private final long speed;

    public InteractableFluidHolderBlock(long speed) {
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
            FluidHolderBlock fluidHolder = FluidHolderBlock.wrapFluidHolderBlock(level, blockPos.relative(direction), direction.getOpposite());
            if (getSideInteraction(direction).isPush() && fluidHolder != null) {
                long push = fluidHolder.push(getFluidStack().copyWithAmount(speed), true);
                fluidHolder.push(pull(Math.min(speed, push), false), false);
            }
        }
    }

    public void handlePull(Level level, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            FluidHolderBlock fluidHolder = FluidHolderBlock.wrapFluidHolderBlock(level, blockPos.relative(direction), direction.getOpposite());
            if (getSideInteraction(direction).isPull() && fluidHolder != null) {
                long push = push(getFluidStack().copyWithAmount(speed), true);
                push(fluidHolder.pull(Math.min(speed, push), false), false);
            }
        }
    }

    @ExpectPlatform
    public static InteractableFluidHolderBlock create(long speed, long capacity) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static InteractableFluidHolderBlock create(long speed, long capacity, Consumer<FluidHolderBlock> onChange) {
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
