package mod.syconn.swe.blockentities;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import mod.syconn.swe.world.container.TankMenu;
import mod.syconn.swe.client.RGBImage;
import mod.syconn.swe.items.extras.ItemFluidHandler;
import mod.syconn.swe.util.FluidHelper;
import mod.syconn.swe.util.ResourceUtil;

public class TankBlockEntity extends GUIFluidHandlerBlockEntity implements MenuProvider {

    private int fillSpeed = 500;
    private RGBImage bfluid;
    private ResourceLocation bfluidLoc;

    private final ItemStackHandler items = new ItemStackHandler(getContainerSize()) {
        @Override
        protected void onContentsChanged(int slot) { update(); }
    };
    private final LazyOptional<IItemHandler> holder = LazyOptional.of(() -> items);

    public TankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.TANK.get(), pos, state, 16000, null);
    }

    protected void updateTextures(FluidStack resource) {
        super.updateTextures(resource);
        bfluid = new RGBImage(ResourceUtil.createFluidBlockTexture(resource.getFluid()));
        bfluidLoc = Minecraft.getInstance().getTextureManager().register("bfluid", bfluid.getImageFromPixels());
        update();
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public ResourceLocation getFluidTexture() {
        return bfluidLoc;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Inventory")) items.deserializeNBT(tag.getCompound("Inventory"));
        if (tag.contains("bfluid")) bfluid = RGBImage.read(tag.getCompound("bfluid"));
        if (bfluid != null) bfluidLoc = Minecraft.getInstance().getTextureManager().register("bfluid", bfluid.getImageFromPixels());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", items.serializeNBT());
        if (bfluid != null) tag.put("bfluid", bfluid.write());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("items", items.serializeNBT());
        if (bfluid != null) tag.put("bfluid", bfluid.write());
        return tag;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        holder.invalidate();
    }

    private int getContainerSize(){
        return 3;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Fluid Tank Screen");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory p_39955_, Player p_39956_) {
        return new TankMenu(id, p_39955_, this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TankBlockEntity e) {
        if (!level.isClientSide) {
            ItemStack heldItem = e.getItems().getStackInSlot(0);
            if (heldItem.getItem() instanceof BucketItem b) {
                int fill = FluidUtil.getFluidHandler(heldItem).map(handler -> e.tank.fill(handler.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE)).orElse(0);
                if (fill > 0) {
                    e.getItems().extractItem(0, 1, false);
                    e.getItems().insertItem(1, new ItemStack(Items.BUCKET), false);
                } else if (b.getFluid() instanceof EmptyFluid && !e.getFluidTank().isEmpty() && !(e.getItems().getStackInSlot(1).getItem() instanceof BucketItem)) {
                    FluidStack fluidStack = FluidUtil.getFluidHandler(heldItem).map(handler -> e.tank.drain(handler.getTankCapacity(0), IFluidHandler.FluidAction.EXECUTE)).orElse(FluidStack.EMPTY);
                    e.getItems().extractItem(0, 1, false);
                    if (fluidStack == FluidStack.EMPTY) e.getItems().insertItem(1, new ItemStack(Items.BUCKET), false);
                    else e.getItems().insertItem(1, FluidUtil.getFilledBucket(fluidStack), false);
                }
            } else if (heldItem.getItem() instanceof ItemFluidHandler && e.getItems().getStackInSlot(1) == ItemStack.EMPTY) {
                e.getItems().extractItem(0, 1, false);
                e.getItems().insertItem(1, FluidHelper.fillTankReturnStack(heldItem, e.tank), false);
            }
            ItemStack item = e.getItems().getStackInSlot(2);
            if (item.getItem() instanceof ItemFluidHandler) {
                FluidHelper.fillHandlerUpdateStack(item, e.tank, e.fillSpeed);
            }
        }
    }
}
