package mod.syconn.swe.common.blockentities;

import dev.architectury.fluid.FluidStack;
import mod.syconn.swe.common.items.FluidHolderItem;
import mod.syconn.swe.core.ModBlockEntities;
import mod.syconn.swe.util.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TankBE extends AbstractTankBE {

    private final SimpleContainer container = new SimpleContainer(3);

    public TankBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TANK.get(), pos, state, 500, FluidStack.bucketAmount() * 16);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TankBE e) {
        if (!level.isClientSide) {
            if (FluidHolderItem.hasFluidHolder(e.container.getItem(0))) FluidUtil.performInventoryTransfer(e.tank, e.container, 0, 1);
            FluidHolderItem fluidHolder = FluidHolderItem.getFluidHolder(e.container, 2);
            if (fluidHolder != null) FluidUtil.transferFluid(fluidHolder, e.tank, FluidStack.bucketAmount() / 10);
            e.tank.handlePush(level, pos);
            e.tank.handlePull(level, pos);
            e.markDirty();
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", this.container.createTag());
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Inventory", 9)) this.container.fromTag(tag.getList("Inventory", 10));
    }
}
