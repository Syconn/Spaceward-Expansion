package mod.syconn.api.blockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public abstract class AbstractPipeBE extends SyncedBE {

    private UUID networkID = null;

    public AbstractPipeBE(BlockEntityType<?> pType, BlockPos pos, BlockState state) {
        super(pType, pos, state);
    }

    public void setNetworkID(UUID uuid) {
        this.networkID = uuid;
        markDirty();
    }

    public UUID getNetworkID() {
        return networkID;
    }

    public abstract boolean canConnectToPipe(Level level, BlockPos pos, Direction conDir);

    protected void saveClientData(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if(networkID != null) tag.putUUID("uuid", networkID);
    }

    protected void loadClientData(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (tag.contains("uuid")) networkID = tag.getUUID("uuid");
    }
}
