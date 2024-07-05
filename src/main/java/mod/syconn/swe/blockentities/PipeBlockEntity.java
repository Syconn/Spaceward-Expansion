package mod.syconn.swe.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import mod.syconn.swe.blocks.FluidBaseBlock;
import mod.syconn.swe.world.container.PipeMenu;
import mod.syconn.swe.items.UpgradeItem;
import mod.syconn.swe.util.Helper;
import mod.syconn.swe.util.data.FluidPointSystem;

import static mod.syconn.swe.util.data.FluidPointSystem.FluidPoint;

public class PipeBlockEntity extends FluidHandlerBlockEntity implements MenuProvider {

    private FluidPointSystem system = new FluidPointSystem();
    private Direction target = null;
    private boolean updated = false;
    private FluidPoint source = FluidPoint.Empty();
    private static final int transferSpeed = 80;

    private final ItemStackHandler items = new ItemStackHandler(3) {
        protected void onContentsChanged(int slot) {
            update();
        }
    };
    private final LazyOptional<IItemHandler> holder = LazyOptional.of(() -> items);

    public PipeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.PIPE.get(), pos, state);
    }

    public FluidPoint getSource() {
        return source;
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public void setSource(FluidPoint source) {
        if (getBlockState().getValue(FluidBaseBlock.ENABLED)) {
            this.source = source;
            this.updated = true;
            update();
            for (Direction d : Direction.values()) {
                if (level.getBlockEntity(worldPosition.relative(d)) instanceof PipeBlockEntity pe && !pe.updated) { // Needs to be where check importance
                    pe.setSource(source);
                }
            }
        }
    }

    public void clear() {
        this.updated = false;
        update();
        for (Direction d : Direction.values()) {
            if (level.getBlockEntity(worldPosition.relative(d)) instanceof PipeBlockEntity pe && pe.updated) {
                pe.clear();
            }
        }
    }

    public void setImporter(BlockPos importer) {
        if (level.getBlockEntity(importer) != null && level.getBlockEntity(importer).getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent() && !(level.getBlockEntity(importer) instanceof PipeBlockEntity)) {
            Direction d = Helper.dirToBlockPos(worldPosition, importer);
            boolean u = system.update(d, new FluidPoint(importer, d, true, 1));
            if (u && target == null) target = d;
            updateStates();
        }
    }

    public void setExporter(BlockPos exporter) {
        if (level.getBlockEntity(exporter) != null && level.getBlockEntity(exporter).getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent() && !(level.getBlockEntity(exporter) instanceof PipeBlockEntity)) {
            Direction d = Helper.dirToBlockPos(worldPosition, exporter);
            boolean u = system.update(d, new FluidPoint(exporter, d, false, 1));
            if (u && target == null) target = d;
            updateStates();
        }
    }

    public void updateStates() {
        source = FluidPoint.Empty();
        for (Direction ds : Direction.values()) {
            if (level.getBlockEntity(worldPosition.relative(ds)) instanceof PipeBlockEntity pe && pe.getSource() != source) {
                pe.setSource(source);
            }
        }
    }

    public FluidPoint selectedTab() {
        return system.getPoint(target);
    }

    public void setTarget(Direction target) {
        this.target = target;
        update();
    }

    // TODO FIXES - Clicking Only Works on Double
    //  - Make it work with Any Fluid Block Not just tanks
    public static void serverTick(Level l, BlockPos pos, BlockState state, PipeBlockEntity be) {
        for (Direction d : Direction.values()) {
            if (l.getBlockEntity(pos.relative(d)) != null && l.getBlockEntity(pos.relative(d)).getCapability(ForgeCapabilities.FLUID_HANDLER, d.getOpposite()).isPresent()) {
                FluidPoint point = be.getSystem().getPoint(d);
                if (point.handlesExport() && (point.priority() > be.getSource().priority() || be.getSource().isEmpty() || point.equals(be.getSource()) || (l.getBlockEntity(be.source.pos()) != null && l.getBlockEntity(be.source.pos()).getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent() && l.getBlockEntity(be.source.pos()).getCapability(ForgeCapabilities.FLUID_HANDLER).resolve().get().getFluidInTank(0).getAmount() <= 0))) {
                    be.setSource(be.getSystem().getPoint(d));
                    be.clear();
                }
            }
        }
        if (!be.source.isEmpty() && l.getBlockState(be.source.pos().relative(be.source.d().getOpposite())).getValue(FluidBaseBlock.ENABLED)) {
            be.source = l.getBlockEntity(be.source.pos().relative(be.source.d().getOpposite()), ModBlockEntity.PIPE.get()).get().getSystem().getPoint(be.source.d());
        } else {
            be.source = FluidPoint.Empty();
        }
        if (!isValidSource(l, be)) {
            be.source = FluidPoint.Empty();
            state = state.setValue(FluidBaseBlock.FLUID_TYPE, false);
            l.setBlock(pos, state, 3);
        } else if (!state.getValue(FluidBaseBlock.FLUID_TYPE)) {
            state = state.setValue(FluidBaseBlock.FLUID_TYPE, true);
            l.setBlock(pos, state, 3);
        }
        if (!be.source.isEmpty() && l.getBlockState(be.source.pos().relative(be.source.d().getOpposite())).getValue(FluidBaseBlock.ENABLED) && state.getValue(FluidBaseBlock.ENABLED)) {
            for (Direction d : Direction.values()) {
                int speed = transferSpeed + speedIncrease(be);
                FluidPoint exporter = be.system.getPoint(d);
                if (!be.source.isEmpty() && !exporter.isEmpty() && l.getBlockEntity(exporter.pos()) != null && l.getBlockEntity(be.source.pos()) != null && !exporter.exporter()) {
                    IFluidHandler export = l.getBlockEntity(exporter.pos()).getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
                    IFluidHandler source = l.getBlockEntity(be.source.pos()).getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);

                    if (export.isFluidValid(0, source.getFluidInTank(0)) && export.getFluidInTank(0).getAmount() / export.getTankCapacity(0) != 1) {
                        FluidStack stack = source.drain(speed, IFluidHandler.FluidAction.EXECUTE);
                        int filled = export.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                        if (filled != stack.getAmount())
                            source.fill(new FluidStack(stack, stack.getAmount() - filled), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
        update(l, pos, state);
    }

    private static boolean isValidSource(Level l, PipeBlockEntity be){
        return !be.source.equals(BlockPos.ZERO) && l.getBlockEntity(be.source.pos()) != null && l.getBlockEntity(be.source.pos()).getCapability(ForgeCapabilities.FLUID_HANDLER).map(f -> f.getFluidInTank(0).getAmount()).orElse(0) > 0;
    }

    public FluidPointSystem getSystem() {
        return system;
    }

    public void increment(int inc) {
        system.increment(target, inc);
        update();
    }

    public void changeType() {
        system.flip(target);
        update();
    }

    private static int speedIncrease(PipeBlockEntity pe){
        int speed = 0;
        for (int i = 0; i < pe.getItems().getSlots(); i++) {
            if (pe.getItems().getStackInSlot(i).getItem() instanceof UpgradeItem u) speed += u.getUpgradeSpeed();
        }
        return speed;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Inventory")) items.deserializeNBT(tag.getCompound("Inventory"));
        if (tag.contains("target")) target = Direction.from3DDataValue(tag.getInt("target"));
        system = new FluidPointSystem(tag.getCompound("system"));
        source = FluidPoint.read(tag.getCompound("source"));
        updated = tag.getBoolean("updated");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (target != null) tag.putInt("target", target.get3DDataValue());
        tag.put("Inventory", items.serializeNBT());
        tag.put("system", system.write());
        tag.put("source", source.write());
        tag.putBoolean("updated", updated);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        if (target != null) tag.putInt("target", target.get3DDataValue());
        tag.put("Inventory", items.serializeNBT());
        tag.put("system", system.write());
        tag.put("source", source.write());
        tag.putBoolean("updated", updated);
        return tag;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        holder.invalidate();
    }

    public void update(){
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
    }

    private static void update(Level level, BlockPos pos, BlockState state){
        setChanged(level, pos, state);
        level.sendBlockUpdated(pos, state, state, 2);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new PipeMenu(p_39954_, p_39955_, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Pipe Screen");
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER)
            return holder.cast();
        return super.getCapability(capability, facing);
    }
}
