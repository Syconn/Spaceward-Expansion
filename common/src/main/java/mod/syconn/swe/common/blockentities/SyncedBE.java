package mod.syconn.swe.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SyncedBE extends BlockEntity { // TODO MAY BE UNNEEDED ANYMORE

    public SyncedBE(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

//    protected void saveAdditional(CompoundTag pTag) {
//        super.saveAdditional(pTag, pRegistries);
//        saveClientData(pTag, pRegistries);
//    }
//
//    protected void loadAdditional(CompoundTag pTag) {
//        super.loadAdditional(pTag);
//        loadClientData(pTag, pRegistries);
//    }
//
//    protected abstract void saveClientData(CompoundTag tag);
//    protected abstract void loadClientData(CompoundTag tag);
//
//    public CompoundTag getUpdateTag() {
//        return saveWithoutMetadata();
//    }
//
//    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
//        CompoundTag tag = super.getUpdateTag(pRegistries);
//        saveClientData(tag, pRegistries);
//        return tag;
//    }
//
//    public ClientboundBlockEntityDataPacket getUpdatePacket() {
//        return ClientboundBlockEntityDataPacket.create(this);
//    }
//
//    public void markDirty() {
//        setChanged();
//        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
//    }
}
