package mod.syconn.api.world.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Map;

public class InteractableFluidTank extends FluidTank implements IFluidHandlerInteractable {

    private final int speed;

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
            if (sided_interactions.get(direction).isPush() && level.getCapability(Capabilities.FluidHandler.BLOCK, blockPos.relative(direction), direction.getOpposite()) != null) {
                IFluidHandler blockHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, blockPos.relative(direction), direction.getOpposite());
                int fill = blockHandler.fill(getFluidInTank(0).copyWithAmount(speed), FluidAction.SIMULATE);
                blockHandler.fill(drain(Math.min(speed, fill), FluidAction.EXECUTE), FluidAction.EXECUTE);
            }
        }
    }

    public void handlePull(Level level, BlockPos blockPos) {

    }

    public FluidTank readFromNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        if (nbt.contains("sided_interactions")) {
            sided_interactions.clear();
            nbt.getList("sided_interactions", Tag.TAG_COMPOUND).forEach(tag -> {
                CompoundTag data = (CompoundTag) tag;
                sided_interactions.put(Direction.from3DDataValue(data.getInt("side")), Interaction.fromI(data.getInt("interaction")));
            });
        }
        return super.readFromNBT(lookupProvider, nbt);
    }

    public CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        super.writeToNBT(lookupProvider, nbt);
        ListTag listTag = new ListTag();
        for (Map.Entry<Direction, Interaction> entry : sided_interactions.entrySet()) {
            CompoundTag data = new CompoundTag();
            data.putInt("side", entry.getKey().get3DDataValue());
            data.putInt("interaction", entry.getValue().i);
            listTag.add(data);
        }
        nbt.put("sided_interactions", listTag);
        return nbt;
    }
}
