package mod.syconn.swe.blockentities;

import cpw.mods.util.Lazy;
import mod.syconn.swe.Registration;
import mod.syconn.swe.util.RGBImage;
import mod.syconn.swe.items.extras.ItemFluidHandler;
import mod.syconn.swe.util.FluidHelper;
import mod.syconn.swe.util.ResourceUtil;
import mod.syconn.swe.world.container.TankMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TankBlockEntity extends GUIFluidHandlerBlockEntity implements MenuProvider {

    private final int fillSpeed = 500;
    private RGBImage bfluid;
    private ResourceLocation bfluidLoc;
    private final ItemStackHandler items = new ItemStackHandler(getContainerSize()) {
        public void onContentsChanged(int slot) { update(); }
    };
    private final Lazy<IItemHandler> holder = Lazy.of(() -> items);

    public TankBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.TANK.get(), pos, state, 16000);
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

    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if (pTag.contains("Inventory")) items.deserializeNBT(pRegistries, pTag.getCompound("Inventory"));
        if (pTag.contains("bfluid")) bfluid = RGBImage.read(pTag.getCompound("bfluid"));
        if (bfluid != null) bfluidLoc = Minecraft.getInstance().getTextureManager().register("bfluid", bfluid.getImageFromPixels());
    }

    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("Inventory", items.serializeNBT(pRegistries));
        if (bfluid != null) pTag.put("bfluid", bfluid.write());
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        tag.put("items", items.serializeNBT(pRegistries));
        if (bfluid != null) tag.put("bfluid", bfluid.write());
        return tag;
    }

    private int getContainerSize(){
        return 3;
    }

    public Component getDisplayName() {
        return Component.literal("Fluid Tank Screen");
    }

    public AbstractContainerMenu createMenu(int id, Inventory p_39955_, Player p_39956_) {
        return new TankMenu(id, p_39955_, this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TankBlockEntity e) {
        if (!level.isClientSide) {
            ItemStack heldItem = e.getItems().getStackInSlot(0);
            if (heldItem.getItem() instanceof BucketItem b) {
                int fill = FluidUtil.getFluidHandler(heldItem).map(handler -> e.tank.fill(handler.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE)).orElse(0);
                if (fill > 0) {
                    e.getItems().extractItem(0, 1, false);
                    e.getItems().insertItem(1, new ItemStack(Items.BUCKET), false);
                } else if (b.content instanceof EmptyFluid && !e.getFluidTank().isEmpty() && !(e.getItems().getStackInSlot(1).getItem() instanceof BucketItem)) {
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

    public IItemHandler getItemHandler() {
        return holder.get();
    }
}
