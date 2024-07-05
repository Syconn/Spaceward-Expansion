package mod.syconn.swe.blockentities;

import mod.syconn.swe.util.ResourceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import mod.syconn.swe.client.RGBImage;

import java.util.List;

public abstract class GUIFluidHandlerBlockEntity extends SidedFluidHandlerBE {

    private RGBImage gfluid;
    private ResourceLocation gfluidLoc;

    public GUIFluidHandlerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int size, List<Direction> side) {
        super(blockEntityType, pos, state, side);

        this.tank = new FluidTank(size){
            @Override
            private void onContentsChanged() { update(); }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                if (fluid.isEmpty()) updateTextures(resource);
                return super.fill(resource, action);
            }
        };
    }

    protected void updateTextures(FluidStack resource) {
        gfluid = new RGBImage(ResourceUtil.createFluidGuiTexture(resource.getFluid()));
        gfluidLoc = Minecraft.getInstance().getTextureManager().register("gfluid", gfluid.getImageFromPixels());
    }

    public ResourceLocation getGuiTexture() {
        return gfluidLoc;
    }

    public FluidTank getFluidTank()
    {
        return this.tank;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("gfluid")) gfluid = RGBImage.read(tag.getCompound("gfluid"));
        if (gfluid != null) gfluidLoc = Minecraft.getInstance().getTextureManager().register("gfluid", gfluid.getImageFromPixels());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tank.writeToNBT(tag);
        if (gfluid != null) tag.put("gfluid", gfluid.write());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tank.writeToNBT(tag);
        if (gfluid != null) tag.put("gfluid", gfluid.write());
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected void update(){
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
    }
}
