package mod.syconn.swe.common.blockentities;

import com.google.common.base.Preconditions;
import dev.architectury.fluid.FluidStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.network.messages.MessageSyncFluidBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class FluidHolderBlock extends FluidHolderItem {

    public abstract void load(CompoundTag tag);
    public abstract void save(CompoundTag tag);

    public ItemStack getContainer() {
        return ItemStack.EMPTY;
    }

    @Environment(EnvType.SERVER)
    public void sync(BlockEntity owner) {
        Level level = Objects.requireNonNull(owner.getLevel());
        Preconditions.checkState(!level.isClientSide());
        if (level.getChunkSource() instanceof ServerChunkCache cache) {
            BlockPos pos = owner.getBlockPos();
            List<ServerPlayer> players = cache.chunkMap.getPlayers(new ChunkPos(pos), false);
            players.forEach(player -> Network.CHANNEL.sendToPlayer(player, new MessageSyncFluidBlock(pos, this.getFluidStack())));
        }
    }

    @Environment(EnvType.CLIENT)
    public final void handleSync(Level level, FluidStack fluidStack) {
        Preconditions.checkState(level.isClientSide());
        this.setFluidStack(fluidStack);
    }

    @ExpectPlatform
    public static FluidHolderBlock create(long capacity) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static FluidHolderBlock create(long capacity, Consumer<FluidHolderBlock> onChange) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean hasHolder(Level level, BlockPos pos, @Nullable Direction face) {
        throw new AssertionError();
    }

    @Nullable
    @ExpectPlatform
    public static FluidHolderBlock getOrWrapFluidHolder(Level level, BlockPos pos, @Nullable Direction face) {
        throw new AssertionError();
    }

    public interface IFluidHolderBlock {
        FluidHolderBlock getFluidHolder();
    }
}
