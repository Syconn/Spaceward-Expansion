package mod.syconn.swe.util.core;

import mod.syconn.swe.extra.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class InteractableFluidTank extends FluidTank implements InteractionalFluidHandler {

    private final int speed;
    Map<Direction, Interaction> sided_interactions = new HashMap<>() {{
        put(Direction.NORTH, Interaction.NONE);
        put(Direction.SOUTH, Interaction.NONE);
        put(Direction.EAST, Interaction.NONE);
        put(Direction.WEST, Interaction.NONE);
        put(Direction.DOWN, Interaction.NONE);
        put(Direction.UP, Interaction.NONE);
    }};

    public InteractableFluidTank(int capacity, int speed) {
        super(capacity);
        this.speed = speed;
    }

    public Interaction getSideInteraction(Direction side) {
        return sided_interactions.get(side);
    }

    public void setSideInteraction(Direction side, Interaction interaction) {
        sided_interactions.put(side, interaction);
    }

    public void handlePush(Level level, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            if (sided_interactions.get(direction).isPush() && Services.FLUID_HANDLER.has(level, blockPos.relative(direction), direction.getOpposite())) {
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
            if (sided_interactions.get(direction).isPull() && Services.FLUID_HANDLER.has(level, blockPos.relative(direction), direction.getOpposite())) {
                FluidHandler blockHandler = Services.FLUID_HANDLER.get(level, blockPos.relative(direction), direction.getOpposite());
                if (!blockHandler.getFluidHolder().isEmpty()) {
                    int fill = fill(getFluidHolder().copyWith(speed), FluidAction.SIMULATE);
                    fill(blockHandler.drain(Math.min(speed, fill), FluidAction.EXECUTE), FluidAction.EXECUTE);
                }
            }
        }
    }

    public FluidHandler readNBT(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        if (tag.contains("sided_interactions")) {
            sided_interactions.clear();
            tag.getList("sided_interactions", Tag.TAG_COMPOUND).forEach(nbt -> {
                CompoundTag data = (CompoundTag) nbt;
                sided_interactions.put(Direction.from3DDataValue(data.getInt("side")), Interaction.fromI(data.getInt("interaction")));
            });
        }
        return super.readNBT(lookupProvider, tag);
    }

    public CompoundTag writeNBT(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        CompoundTag tag1 = super.writeNBT(lookupProvider, tag);
        ListTag listTag = new ListTag();
        for (Map.Entry<Direction, Interaction> entry : sided_interactions.entrySet()) {
            CompoundTag data = new CompoundTag();
            data.putInt("side", entry.getKey().get3DDataValue());
            data.putInt("interaction", entry.getValue().i);
            listTag.add(data);
        }
        tag1.put("sided_interactions", listTag);
        return tag1;
    }
}
