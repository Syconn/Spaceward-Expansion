package mod.syconn.swe.blockentities;

import mod.syconn.swe.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import mod.syconn.swe.util.RGBImage;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public abstract class GUIFluidHandlerBlockEntity extends SidedFluidHandlerBE { // TODO GET RID OF TEXTURE UTIL AND DO IT ON CLIENT

    protected RGBImage gfluid;
    protected ResourceLocation gfluidLoc;

    public GUIFluidHandlerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int size) {
        super(blockEntityType, pos, state);

        this.tank = new FluidTank(size) {
            public void onContentsChanged() { update(); }

            public int fill(FluidStack resource, FluidAction action) {
                if (fluid.isEmpty()) updateTextures(resource);
                return super.fill(resource, action);
            }
        };
    }

    protected void updateTextures(FluidStack resource) {
        if (FMLEnvironment.dist.isClient()) {
            gfluid = new RGBImage(RenderUtil.createFluidGuiTexture(resource.getFluid()));
            gfluidLoc = Minecraft.getInstance().getTextureManager().register("gfluid", gfluid.getImageFromPixels());
            update();
        }
    }

    public ResourceLocation getGuiTexture() {
        return gfluidLoc;
    }

    public FluidTank getFluidTank() {
        return this.tank;
    }

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("gfluid")) gfluid = RGBImage.read(pTag.getCompound("gfluid"));
        if (gfluid != null) gfluidLoc = Minecraft.getInstance().getTextureManager().register("gfluid", gfluid.getImageFromPixels());
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        tank.writeToNBT(pRegistries, pTag);
        if (gfluid != null) pTag.put("gfluid", gfluid.write());
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        tank.writeToNBT(pRegistries, tag);
        if (gfluid != null) tag.put("gfluid", gfluid.write());
        return tag;
    }

    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected void update(){
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
    }
}
