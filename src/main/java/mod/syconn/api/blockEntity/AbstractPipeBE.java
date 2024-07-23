package mod.syconn.api.blockEntity;

import mod.syconn.swe.blockentities.TankBE;
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

    public abstract void tickServer(Level level, BlockPos pos, BlockState state, TankBE e);

    public void setNetworkID(UUID uuid) {
        this.networkID = uuid;
        markDirty();
    }

    public UUID getNetworkID() {
        return networkID;
    }

    public boolean canConnect(Level level, BlockPos pos, Direction conDir) {
        return true;
    }

    protected void saveClientData(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if(networkID != null) tag.putUUID("uuid", networkID);
    }

    protected void loadClientData(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (tag.contains("uuid")) networkID = tag.getUUID("uuid");
    }
}
