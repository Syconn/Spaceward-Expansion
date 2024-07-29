package mod.syconn.api.blockEntity;

import mod.syconn.api.blocks.AbstractPipeBlock;
import mod.syconn.api.blocks.BaseFluidPipe;
import mod.syconn.api.util.PipeConnectionTypes;
import mod.syconn.api.world.data.savedData.PipeNetworks;
import mod.syconn.swe.Registration;
import mod.syconn.swe.blockentities.TankBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseFluidPipeBE extends AbstractPipeBE {

    private Fluid fluid = null;

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
        return getBlockState().getValue(AbstractPipeBlock.fromDirection(direction));
    }

    public void setConnectionType(Direction direction, PipeConnectionTypes type) {
        level.setBlock(worldPosition, getBlockState().setValue(AbstractPipeBlock.fromDirection(direction), type), 2);
    }

    protected void saveClientData(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveClientData(tag, pRegistries);
        if (fluid != null) tag.putInt("fluid", BuiltInRegistries.FLUID.getId(fluid));
    }

    protected void loadClientData(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadClientData(tag, pRegistries);
        if (tag.contains("fluid")) fluid = BuiltInRegistries.FLUID.byId(tag.getInt("fluid"));
    }

    public void setFluid(@Nullable Fluid fluid) {
        this.fluid = fluid;
    }

    public boolean hasFluid() {
        return fluid != null;
    }

    public FluidStack getFluid() {
        return new FluidStack(fluid, 1000);
    }
}
